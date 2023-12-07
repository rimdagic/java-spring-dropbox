package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * This `ResponseMessageDto` class represents a data transfer object (DTO) with attributs that holds a message and a
 * timestamp. Instances of the class are used to provide the user with timestamped messages when returning response
 * entities. This class has an initializing constructor so instances can be created.
 */
@AllArgsConstructor
@Getter
public class ResponseMessageDto {

    private String message;
    private LocalDateTime timestamp;
}
