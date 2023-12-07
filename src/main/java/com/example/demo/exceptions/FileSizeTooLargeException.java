package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The `FileSizeTooLargeException` class represents an exception that is thrown when attempting
 * to save a file with a file size that is larger than a set value.
 * <p>
 * This exception is annotated with @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE), in case it is not caught and handled,
 * it will result in an HTTP 413 Payload too large response.
 */
@ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
public class FileSizeTooLargeException extends RuntimeException {

    /**
     * Constructs a new `FileSizeTooLargeException` with the specified detail message.
     *
     * @param message A message providing more details about the exception, passing it to the super class
     *                `RuntimeException`.
     */
    public FileSizeTooLargeException(String message) {
        super(message);
    }
}