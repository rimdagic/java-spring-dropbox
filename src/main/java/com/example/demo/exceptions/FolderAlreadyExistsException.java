package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The `FolderAlreadyExistsException` class represents an exception that is thrown when attempting
 * to create a folder with name that already exists in the current user account.
 * <p>
 * This exception is annotated with @ResponseStatus(HttpStatus.CONFLICT), in case it is not caught and handled,
 * it will result in an HTTP 409 Conflict response.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class FolderAlreadyExistsException extends RuntimeException{
        public FolderAlreadyExistsException(String message) {
            super(message);
        }
    }