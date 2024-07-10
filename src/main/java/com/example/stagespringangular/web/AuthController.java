package com.example.stagespringangular.web;


import com.example.stagespringangular.dtos.AuthRequestDTO;
import com.example.stagespringangular.dtos.AuthResponseDTO;
import com.example.stagespringangular.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin( "http://localhost:4200")

public class AuthController {

    private  AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<Object> authenticateUser(@RequestBody AuthRequestDTO authRequest) {

        String token = authService.verifyUserCredentials(authRequest);

        if (token != null) {
            AuthResponseDTO authResponse = new AuthResponseDTO(token);
            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }


}
