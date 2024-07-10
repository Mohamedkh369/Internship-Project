package com.example.stagespringangular.web;
import com.example.stagespringangular.dtos.UserDTO;
import com.example.stagespringangular.services.KeyCloakAdminService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keycloak-api/test")
public class DemoController {

    @Autowired
    private KeyCloakAdminService keyCloakAdminService;

    public DemoController(KeyCloakAdminService keycloakAdminService) {
        this.keyCloakAdminService = keycloakAdminService;
    }


    @PostMapping("create")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        String result = keyCloakAdminService.createUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(),
                userDTO.getFirstName(), userDTO.getLastName());
        if (result.equals("User created successfully")) {
            return ResponseEntity.ok(result);
        } else {
            System.out.println("zzzzzzzzzzzzzzzzzzzz");
            System.out.println(userDTO.toString());
            return ResponseEntity.status(500).body(result);

        }
    }

    @GetMapping("view-users")
    public ResponseEntity<List<UserRepresentation>> getUsers() {
        List<UserRepresentation> users = keyCloakAdminService.getUsers();
        return ResponseEntity.ok(users);
    }


    @DeleteMapping("delete-user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
        keyCloakAdminService.deleteUser(userId);
        return ResponseEntity.ok().body("{\"message\": \"User deleted successfully\"}");
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());

        try {
            keyCloakAdminService.updateUser(userId, userRepresentation);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getUser-id/{userId}")
    public UserDTO getUserById(@PathVariable("userId") String userId) {
        return keyCloakAdminService.getUserById(userId) ;
    }

}
