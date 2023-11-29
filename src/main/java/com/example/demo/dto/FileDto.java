package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class FileDto {

    private UUID id;
    private String name;
    private byte[] data;
    private Date created;
    private UUID folder;
}
