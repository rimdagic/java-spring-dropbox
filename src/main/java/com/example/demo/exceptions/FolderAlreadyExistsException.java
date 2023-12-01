package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FolderAlreadyExistsException extends RuntimeException{
        public FolderAlreadyExistsException(String message) {
            super(message);
        }
    }