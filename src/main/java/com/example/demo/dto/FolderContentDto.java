package com.example.demo.dto;

import com.example.demo.models.File;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * This `FolderContentDto` class represents a data transfer object (DTO) holding the content of a specific folder.
 * Instances of FolderContentDto are used to retrieve a list of files from the FileRepository in a specific folder.
 * It is annotated with Getter and Setter so that attributes of its instances can be set and retrieved.
 */
@Getter
@Setter
public class FolderContentDto {
    private String folderName;
    private Collection<File> files;

    /**
     * A constructor that initializes instances of the FolderContentDto class
     * @param folderName A folder name
     * @param files A collection of the files that the folder contains
     */
    public FolderContentDto(String folderName, List<File> files) {
        this.folderName = folderName;
        this.files = files;
    }
}
