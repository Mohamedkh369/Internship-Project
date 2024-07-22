package com.example.stagespringangular.dtos;

import lombok.Data;

@Data
public class EmailRequest {
    private String to;
    private String username;
    private String verificationURL;
}
