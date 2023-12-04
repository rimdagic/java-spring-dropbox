package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class FileAlreadyExistsException extends RuntimeException{
        public FileAlreadyExistsException(String message) {
            super(message);
        }
    }