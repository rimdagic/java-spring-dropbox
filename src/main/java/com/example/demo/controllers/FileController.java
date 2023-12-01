package com.example.demo.controllers;

import com.example.demo.dto.CreateFileDto;
import com.example.demo.models.File;
import com.example.demo.services.FileService;
import com.example.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/{folder}")
public class FileController {

    FileService fileService;
    UserService userService;

    public FileController(FileService fileService, UserService userService){
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/{file}")
    public String getFile(@PathVariable String folder, @PathVariable("file") String fileName) {

        return "File: " + fileName + " in folder: " + folder;
    }

    @PostMapping("/{folderName}")
    public ResponseEntity<File> saveFile(
            @RequestParam("file") MultipartFile multipartFile,
            @PathVariable String folderName,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        var username = userService.getUsernameFromToken(authorizationHeader);
        var result = fileService.saveFile(multipartFile, folderName, username);
        return ResponseEntity.ok(result);
    }
}
