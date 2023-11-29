package com.example.demo.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateFolderDto {

    private String name;
    private UUID ownerId;
}
