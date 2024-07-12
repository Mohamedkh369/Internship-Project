package com.example.stagespringangular.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {

    private String name;
    private Map<String, List<String>> attributes;}


