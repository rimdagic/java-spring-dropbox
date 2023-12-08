package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The `AccountAlreadyExistsException` class is an exception that is thrown when attempting
 * to create an account with a username that already exists.
 * <p>
 * This exception is annotated with @ResponseStatus(HttpStatus.CONFLICT), in case it is not caught and handled,
 * it will result in an HTTP 409 Conflict response.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class AccountAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new `AccountAlreadyExistsException` with a specified detail message.
     *
     * @param message A message providing more details about the exception, passing it to the super class
     *                `RuntimeException`.
     */
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}