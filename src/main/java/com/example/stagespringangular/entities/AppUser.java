package com.example.stagespringangular.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class AppUser {

    @Id
    @Column
    private String username;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private boolean enabled = false;

    public AppUser() {}

    public AppUser (String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.enabled = false;
    }


}
