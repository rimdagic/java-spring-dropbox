package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * This `CreateFileDto` class represents a data transfer object (DTO) used for creating a new file.
 * Instances of CreateFileDto are used to handle logic before saving the data as a File in the FileRepository.
 * It is annotated with Getter so that information about the CreateFileDto instances can be retrieved. The class also
 * have a constructor taking in all arguments when new instances are initialized. The attributes store the name of the
 * file to be created, the data of the file to be created and the folderId store the UUID of the folder that are going
 * to hold the file.
 */
@AllArgsConstructor
@Getter
public class CreateFileDto {

    private String name;
    private byte[] data;
    private UUID folderId;
}
