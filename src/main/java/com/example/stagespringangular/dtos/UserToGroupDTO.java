package com.example.stagespringangular.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserToGroupDTO {

    private String username;
    private String groupName;
}
