package com.example.stagespringangular.services;

import com.example.stagespringangular.dtos.AuthRequestDTO;
import lombok.Getter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${keycloak.server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    @Getter
    private String accessToken;

    public String  verifyUserCredentials(AuthRequestDTO authRequest) {
         String username = authRequest.getUsername();
        String password = authRequest.getPassword();
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakAuthServerUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .build();

        try {
            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
            accessToken = tokenResponse.getToken();
            return accessToken;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

}
