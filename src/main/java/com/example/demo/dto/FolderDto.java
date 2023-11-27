package com.example.demo.dto;

import com.example.demo.models.Folder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class FolderDto {
    private UUID id;
    private UUID ownerId;
    private String name;
    private Date created;

    public FolderDto(Folder folder) {
        this.id = folder.getId();
        this.ownerId = folder.getOwnerId();
        this.name = folder.getName();
        this.created = folder.getCreated();

    }
}
