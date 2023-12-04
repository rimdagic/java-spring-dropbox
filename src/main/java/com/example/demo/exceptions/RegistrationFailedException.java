package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class RegistrationFailedException extends RuntimeException{
        public RegistrationFailedException(String message) {
            super(message);
        }
    }