package com.example.stagespringangular.web;


import com.example.stagespringangular.dtos.NewPaymentDTO;
import com.example.stagespringangular.entities.Payment;
import com.example.stagespringangular.entities.PaymentStatus;
import com.example.stagespringangular.entities.PaymentType;
import com.example.stagespringangular.entities.Student;
import com.example.stagespringangular.repository.PaymentRepository;
import com.example.stagespringangular.repository.StudentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin( "*")

public class PaymentRestController {

    private PaymentRepository paymentRepository ;
    private StudentRepository studentRepository;
    public  PaymentRestController(PaymentRepository paymentRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository ;
        this.studentRepository = studentRepository ;
    }
@GetMapping(path = "/Payments")
    public List<Payment> allPayments(){
        return paymentRepository.findAll();
    }

    @GetMapping(path="/Payments/{id}")
    public Payment getPaymentById(@PathVariable long id){
        return paymentRepository.findById(id).get();
    }

    @GetMapping(path="/Students")
    public List <Student> allStudents(){
        return studentRepository.findAll();
    }

    @GetMapping(path="/Students/{code}")
    public Student getStudentById(@PathVariable String code){
        return studentRepository.findByCode(code);
    }

    @GetMapping(path = "/StudentsByProgramId")
    public List<Student> getStudentsByProgramId(@RequestParam String programId){

        return studentRepository.findStudentByProgramId(programId);
    }

    @GetMapping(path="Students/{code}/Payments")
    public List<Payment> getPaymentsByStudentcode(@PathVariable String code){
       return  paymentRepository.findByStudentCode(code);

    }

    @GetMapping(path="/PaymentByStatus")
    public List<Payment> getPaymentsByPaymentStatus(@RequestParam  PaymentStatus paymentStatus){
        return  paymentRepository.findByStatus(paymentStatus);

    }

    @GetMapping(path="/Payments/Type/{paymentType}")
    public List<Payment> getPaymentsByType(@PathVariable PaymentType paymentType){
        return  paymentRepository.findByType(paymentType);

    }
    @PutMapping (path = "/Payments/Update/{id}")
    public Payment updatePaymentStatus(@PathVariable long id, @RequestParam PaymentStatus paymentStatus){
        Payment payment = paymentRepository.findById(id).get();
        payment.setStatus(paymentStatus);
        return paymentRepository.save(payment);
    }

@PostMapping(path = "/NewPayments",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Payment  savePayment(@RequestParam("file") MultipartFile file , NewPaymentDTO newPaymentDto) throws IOException {
        Path folderPath = Paths.get(System.getProperty("user.home"),"Stage_Spring_Angular","Payments");
        if(!Files.exists(folderPath)){
            Files.createDirectories(folderPath);

        }
        String fileName = UUID.randomUUID().toString();
        Path filePath = Paths.get(System.getProperty("user.home"),"Stage_Spring_Angular","Payments",fileName+".pdf");
        Files.copy(file.getInputStream(),filePath);
        Payment payment =Payment.builder()
                .date(newPaymentDto.getDate())
                .type(newPaymentDto.getType())
                .student(studentRepository.findByCode(newPaymentDto.getStudentCode()))
                .amount(newPaymentDto.getAmount())
                .file(filePath.toUri().toString())
                .status(PaymentStatus.CREATED)
                .build();
        return paymentRepository.save(payment);
    }

    @GetMapping(path = "/PaymentFile/{paymentId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPaymentFile(@PathVariable long paymentId) throws IOException {
        Payment payment =paymentRepository.findById(paymentId).get();
        return Files.readAllBytes(Path.of(URI.create(payment.getFile())));

    }



}

