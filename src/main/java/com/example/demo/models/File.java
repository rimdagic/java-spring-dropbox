package com.example.demo.models;

import com.example.demo.dto.CreateFileDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * The `File` class represents the File entity.
 * The class holds five different attributes ant each instance of the class represents a file in the database.
 * <p>
 * -id, of the UUID type is used to identify a specific file since it is the only unique attribute.
 * -name, filename retrieved from the uploaded file.
 * -data, this attribute store the file data. It's annotation specify the column type that is to be used to
 *      store larger files in the database. The file data will not be represented in Json
 * -dateCreated, stores a pont in time when the file was first uploaded.
 * -folderId, contains a UUID that represents the folder that the file are stored in.
 * <p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Lob
    @Column(name = "data", nullable = false, columnDefinition = "LONGBLOB")
    @JsonIgnore
    private byte[] data;

    private Date created = new Date();

    @Column(name = "folder_id")
    private UUID folderId;

    /**
     * Constructs a new `File` using information from the provided `CreateFileDto`.
     * Copies the name, data, and folderId from the DTO to initialize the file.
     *
     * @param createFileDto The data transfer object containing file information.
     */
    public File(CreateFileDto createFileDto){
        this.name = createFileDto.getName();
        this.data = createFileDto.getData();
        this.folderId = createFileDto.getFolderId();
    }
}
