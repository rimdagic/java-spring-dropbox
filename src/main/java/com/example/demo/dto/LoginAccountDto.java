package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This `LoginAccountDto` class represents a data transfer object (DTO) holding the login credentials of a specific
 * account while user tries to log in. Instances of LoginAccountDto are used to get the login attempt username and
 * password and authenticate it so the user can be provided with a valid JWT.
 */
@AllArgsConstructor
@Getter
public class LoginAccountDto {
    private String username;
    private String password;
}
