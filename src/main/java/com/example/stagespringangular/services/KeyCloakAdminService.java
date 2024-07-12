package com.example.stagespringangular.services;

import com.example.stagespringangular.dtos.GroupDTO;
import com.example.stagespringangular.dtos.RoleDTO;
import com.example.stagespringangular.dtos.UserDTO;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeyCloakAdminService {

    private Keycloak keycloak;
    @Value("${keycloak.server-url}")
    private  String serverUrl ;

    @Value("${keycloak.realm}")
    private  String realm ;

    @Value("${keycloak.client-id}")
    private final String clientId = "stage_client";

    @Value("${keycloak.clientSecret}")
    private  String clientSecret ;

    @Value("${keycloak.username}")
    private  String adminUsername ;

    @Value("${keycloak.password}")
    private String adminPassword ;

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("TestRealm")
                .clientId("stage_client")
                .username(adminUsername)
                .password(adminPassword)
                .clientSecret(clientSecret)
                .build();
    }

    public String createUser(String username, String password, String email , String firstName, String lastName) {
        UserRepresentation user = new UserRepresentation();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setUsername(username);
        user.setEmail(email);
        user.setRealmRoles(Collections.singletonList("user"));  // Assigning a default role

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(Collections.singletonList(credential));

        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() == 201) {
            return "User created successfully";
        } else {
            return "Failed to create user: " + response.getStatusInfo().getReasonPhrase();
        }
    }

    public List<UserRepresentation> getUsers(){

        return keycloak.realm(realm).users().list();

    }


    public void deleteUser(String userId) {
        keycloak.realm(realm).users().get(userId).remove();
    }

    public void updateUser(String userId, UserRepresentation userRepresentation) {
        keycloak.realm(realm)
                .users()
                .get(userId)
                .update(userRepresentation);
    }
    public UserDTO getUserById(String userId) {
        return mapToUserDTO ( keycloak.realm(realm).users().get(userId).toRepresentation());
    }

    private UserDTO mapToUserDTO(UserRepresentation userRepresentation) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userRepresentation.getUsername());
        userDTO.setEmail(userRepresentation.getEmail());
        userDTO.setFirstName(userRepresentation.getFirstName());
        userDTO.setLastName(userRepresentation.getLastName());
        return userDTO;
    }

    public List<GroupDTO> getGroups() {
        List<GroupRepresentation> groups = keycloak.realm(realm).groups().groups();
        return groups.stream()
                .map(groupRepresentation -> {
                    GroupDTO groupDTO = new GroupDTO();
                    groupDTO.setName(groupRepresentation.getName());
                    groupDTO.setAttributes(groupRepresentation.getAttributes());
                    return groupDTO;
                })
                .collect(Collectors.toList());
    }

    public List<RoleDTO> getClientRoles(String clientId) {
        RealmResource realmResource = keycloak.realm(realm);
        ClientResource clientResource = realmResource.clients().get(clientId);
        List<RoleRepresentation> roles =clientResource.roles().list();

        List<RoleDTO> roleDTOs = new ArrayList<>();

        for (RoleRepresentation role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setName(role.getName());
            roleDTO.setDescription(role.getDescription());
            roleDTO.setComposite(role.isComposite());
            roleDTOs.add(roleDTO);
        }

            return roleDTOs;
        }

    public List<RoleDTO> getRealmRoles(String cliendid) {
        List<RoleRepresentation> roles = keycloak.realm(realm).roles().list();
        List<RoleDTO> roleDTOs = new ArrayList<>();

        for (RoleRepresentation role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setName(role.getName());
            roleDTO.setDescription(role.getDescription());
            roleDTO.setComposite(role.isComposite());
            roleDTOs.add(roleDTO);
        }



        return roleDTOs;

    }

    public void createRole(RoleDTO roleDTO) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(roleDTO.getName());
        roleRepresentation.setDescription(roleDTO.getDescription());
        roleRepresentation.setComposite(false);
        keycloak.realm(realm).roles().create(roleRepresentation);
    }

    public void createGroup(String groupName) {
        RealmResource realmResource = keycloak.realm(realm);

        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupName);

        realmResource.groups().add(groupRepresentation);
    }


}
