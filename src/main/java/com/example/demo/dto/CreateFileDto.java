package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CreateFileDto {

    private String name;
    private byte[] data;
    private UUID folder;
}
