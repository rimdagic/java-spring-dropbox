package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingAccountException extends RuntimeException{
        public MissingAccountException(String message) {
            super(message);
        }
    }