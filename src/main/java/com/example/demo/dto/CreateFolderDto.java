package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * This `CreateFolderDto` class represents a data transfer object (DTO) used for creating a new folder.
 * Instances of CreateFolderDto are used to handle logic before saving the folder in the FolderRepository and database.
 * It is annotated with Getter and Setter so that attributes of its instances can be set and retrieved.
 */
@Getter
@Setter
public class CreateFolderDto {

    private String name;
    private UUID ownerId;
}
