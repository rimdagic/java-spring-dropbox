package com.example.demo.dto;

import com.example.demo.models.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
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
