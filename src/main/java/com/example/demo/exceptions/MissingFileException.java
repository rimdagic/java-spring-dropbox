package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The `MissingFileException` class represents an exception that is thrown when attempting
 * to retrieve a file with a file name that does not exist in a specific folder that is owned by the current user
 * in the FileRepository.
 * <p>
 * This exception is annotated with @ResponseStatus(HttpStatus.NOT_FOUND), in case it is not caught and handled,
 * it will result in an HTTP 404 Not found response.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MissingFileException extends RuntimeException{
        public MissingFileException(String message) {
            super(message);
        }
    }