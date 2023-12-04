package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MissingFolderException extends RuntimeException{
        public MissingFolderException(String message) {
            super(message);
        }
    }