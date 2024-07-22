package com.example.stagespringangular.services;

import com.example.stagespringangular.entities.AppUser;
import com.example.stagespringangular.entities.VerificationToken;
import com.example.stagespringangular.repository.AppUserRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.example.stagespringangular.repository.VerificationTokenRepository;
import com.example.stagespringangular.repository.AppUserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private final JavaMailSender javaMailSender;


    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;

    }

    public void sendVerificationEmail(String to, String username, String verificationURL) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mohamedkhlifi97@gmail.com");
        message.setTo(to);
        message.setSubject("User Registration Confirmation");
        message.setText("Dear " + username + ",\n\n"
                + "Thank you for registering. Please click on the below link to activate your account:\n"
                + verificationURL );

        javaMailSender.send(message);
    }


    public ResponseEntity<String> verifyAccount( String token) {
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);

        if (!optionalToken.isPresent()) {
            return ResponseEntity.status(400).body("Invalid verification token");
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body("Verification token has expired");
        }

        AppUser appUser = verificationToken.getAppUser();
        appUser.setEnabled(true);
        appUserRepository.save(appUser);



        return ResponseEntity.ok("Account verified successfully");
    }
}
