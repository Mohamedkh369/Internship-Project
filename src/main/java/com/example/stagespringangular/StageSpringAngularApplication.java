package com.example.stagespringangular;

import com.example.stagespringangular.entities.Payment;
import com.example.stagespringangular.entities.PaymentStatus;
import com.example.stagespringangular.entities.PaymentType;
import com.example.stagespringangular.entities.Student;
import com.example.stagespringangular.repository.PaymentRepository;
import com.example.stagespringangular.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class StageSpringAngularApplication {

    public static void main(String[] args) {
        SpringApplication.run(StageSpringAngularApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(StudentRepository studentRepository , PaymentRepository paymentRepository) {
        return args -> {
            studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).firstName("Mohamed")
                    .lastName("khlifi").code("12345").programId("Gl").build());

            studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).firstName("hamed")
                    .lastName("hamden").code("12785").programId("Gl").build());

            studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).firstName("Oumayma")
                    .lastName("Hosni").code("12305").programId("DW").build());

            studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).firstName("ahmed")
                    .lastName("ben romdhan").code("11125").programId("Dw").build());
            Random random = new Random();

            PaymentType[] paymentTypes = PaymentType.values();
            studentRepository.findAll().forEach(student -> {
                for (int i = 0; i <= 10; i++) {
                    int index = random.nextInt(paymentTypes.length);
                    Payment payment = Payment.builder()
                            .amount(1000 + (int) (Math.random() * 20000)).type(paymentTypes[index])
                            .status(PaymentStatus.CREATED)
                            .date(LocalDate.now())
                            .student(student)
                            .build();
                    paymentRepository.save(payment);
                }
            });

        };

    }
}
