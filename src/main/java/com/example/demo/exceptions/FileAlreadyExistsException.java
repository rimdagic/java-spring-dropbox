package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The `FileAlreadyExistsException` class is an exception that is thrown when attempting
 * to save a file with a file name that already exists.
 * <p>
 * This exception is annotated with @ResponseStatus(HttpStatus.CONFLICT), in case it is not caught and handled,
 * it will result in an HTTP 409 Conflict response.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class FileAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new `FileAlreadyExistsException` with a specified detail message.
     *
     * @param message A message providing more details about the exception, passing it to the super class
     *                `RuntimeException`.
     */
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}