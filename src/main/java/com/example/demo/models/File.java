package com.example.demo.models;

import com.example.demo.dto.CreateFileDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private byte[] data;
    private Date created = new Date();

    @Column(name = "folder_id")
    private UUID folder;

    public File(CreateFileDto createFileDto){
        this.name = createFileDto.getName();
        this.data = createFileDto.getData();
        this.folder = createFileDto.getFolder();
    }
}
