package com.whu.ontologybackend.entity;

import lombok.*;
// a test class for mybatis

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
