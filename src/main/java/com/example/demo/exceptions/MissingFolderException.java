package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The `MissingFolderException` class represents an exception that is thrown when attempting
 * to retrieve a folder with a name that does not exist in the FolderRepository and is owned by the current user account.
 * <p>
 * This exception is annotated with @ResponseStatus(HttpStatus.NOT_FOUND), in case it is not caught and handled,
 * it will result in an HTTP 404 Not found response.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MissingFolderException extends RuntimeException {

    /**
     * Constructs a new `MissingFolderException` with a specified detail message.
     *
     * @param message A message providing more details about the exception, passing it to the super class
     *                `RuntimeException`.
     */
    public MissingFolderException(String message) {
        super(message);
    }
}