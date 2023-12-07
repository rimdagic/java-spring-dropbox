package com.example.demo.dto;

import com.example.demo.models.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public FolderContentDto(String folderName, List<File> files) {
        this.folderName = folderName;
        this.files = files;
    }
}
