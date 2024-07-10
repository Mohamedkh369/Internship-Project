package com.example.stagespringangular.dtos;

import com.example.stagespringangular.entities.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class NewPaymentDTO {

    private LocalDate date ;
    private double amount ;
    private PaymentType type;
    private String studentCode;
}
