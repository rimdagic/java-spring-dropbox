package com.example.demo.controllers;

import com.example.demo.models.File;
import com.example.demo.services.FileService;
import com.example.demo.services.UserService;
import jakarta.activation.FileTypeMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    FileService fileService;
    UserService userService;

    public FileController(FileService fileService, UserService userService){
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/{file}")
    public ResponseEntity<byte[]> getFile(@PathVariable("file") String fileName) {
        File fileBytes = fileService.loadFileAsBytes(fileName);

        String contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(fileName);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        byte[] resource = fileBytes.getData();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.parseMediaType(contentType));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
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
