package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateAccountDto {
    private String username;
    @Setter
    private String password;

}
