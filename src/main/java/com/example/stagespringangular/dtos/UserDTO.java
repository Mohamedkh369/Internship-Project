package com.example.stagespringangular.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString

public class UserDTO {


        @Setter
        @Getter
        private String username;
        @Setter
        @Getter
        private String password;
        @Setter
        @Getter
        private String email;
        @Setter
        @Getter
        private   String firstName;
        @Getter
        @Setter
        private   String lastName;
}
