package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The `RegistrationFailedException` class represents an exception that is thrown when attempting
 * to saving a new account to the account repository fails. This typically happens when the account has the same
 * username as an account that already exists in the account repository.
 * <p>
 * This exception is annotated with @ResponseStatus(HttpStatus.CONFLICT), in case it is not caught and handled,
 * it will result in an HTTP 409 Conflict response.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class RegistrationFailedException extends RuntimeException {

    /**
     * Constructs a new `RegistrationFailedException` with a specified detail message.
     *
     * @param message A message providing more details about the exception, passing it to the super class
     *                `RuntimeException`.
     */
    public RegistrationFailedException(String message) {
        super(message);
    }
}