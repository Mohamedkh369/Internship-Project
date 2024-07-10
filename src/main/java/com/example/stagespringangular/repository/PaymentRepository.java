package com.example.stagespringangular.repository;

import com.example.stagespringangular.entities.Payment;
import com.example.stagespringangular.entities.PaymentStatus;
import com.example.stagespringangular.entities.PaymentType;
import com.example.stagespringangular.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository <Payment, Long> {


    List<Payment> findByStudentCode(String code);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByType(PaymentType type);



}
