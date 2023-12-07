package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The `CreateAccountDto` class represents a data transfer object (DTO) used for creating a new account.
 * It is annotated with Getter and Setter but does not have an initializing constructor.
 */
@Getter
@Setter
public class CreateAccountDto {
    private String username;
    private String password;

}
