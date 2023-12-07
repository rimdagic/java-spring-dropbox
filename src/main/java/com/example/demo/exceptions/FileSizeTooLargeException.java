package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
public class FileSizeTooLargeException extends RuntimeException{
        public FileSizeTooLargeException(String message) {
            super(message);
        }
    }