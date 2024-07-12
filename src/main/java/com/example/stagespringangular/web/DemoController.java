package com.example.stagespringangular.web;
import com.example.stagespringangular.dtos.GroupDTO;
import com.example.stagespringangular.dtos.RoleDTO;
import com.example.stagespringangular.dtos.UserDTO;
import com.example.stagespringangular.services.KeyCloakAdminService;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
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

    @GetMapping("/getGroups")
    public List<GroupDTO> getGroups() {
        return keyCloakAdminService.getGroups();
    }

    @GetMapping("/roles")
    public List<RoleDTO> getRealmRoles() {
        return keyCloakAdminService.getRealmRoles("stage_client");
    }

    @PostMapping("/createRole")
    public ResponseEntity<String> createRole(@RequestBody RoleDTO roleDTO) {
        keyCloakAdminService.createRole(roleDTO);
        return ResponseEntity.ok("Role created successfully");
    }



    @PostMapping("/createGroup")
    public ResponseEntity<String> createGroup(@RequestBody String groupName) {
        keyCloakAdminService.createGroup(groupName);
        return ResponseEntity.ok("Group created successfully");
    }



}
