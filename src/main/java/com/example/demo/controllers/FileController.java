package com.example.demo.controllers;

import com.example.demo.dto.ResponseMessageDto;
import com.example.demo.models.File;
import com.example.demo.services.FileService;
import com.example.demo.services.UserService;
import jakarta.activation.FileTypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/file")
public class FileController {

    FileService fileService;
    UserService userService;

    @Autowired
    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/{folderName}/{filename}")
    public ResponseEntity<Object> getFile(
            @PathVariable("folderName") String folderName,
            @PathVariable("filename") String filename,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        var username = userService.getUsernameFromToken(authorizationHeader);

        try {
            File fileToDownload = fileService.downloadFile(username, folderName, filename);
            String contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(fileToDownload.getName());

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", fileToDownload.getName());
            headers.setContentType(MediaType.parseMediaType(contentType));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileToDownload.getData());

        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ResponseMessageDto(e.getMessage(), LocalDateTime.now()));
        }
    }

    @PostMapping("/{folderName}")
    public ResponseEntity<Object> saveFile(
            @RequestParam("file") MultipartFile multipartFile,
            @PathVariable String folderName,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        var username = userService.getUsernameFromToken(authorizationHeader);

        try {
            var result = fileService.saveFile(multipartFile, folderName, username);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new ResponseMessageDto(e.getMessage(), LocalDateTime.now()));
        }
    }

    @DeleteMapping({"/{folderName}/{filename}"})
    public ResponseEntity<Object> deleteFile(
            @PathVariable String folderName,
            @PathVariable String filename,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        var username = userService.getUsernameFromToken(authorizationHeader);
        try {
            var deletedFile = fileService.deleteFile(username, folderName, filename);
            return ResponseEntity.ok(deletedFile);

        } catch (Exception e) {
            return ResponseEntity.status(404).body(
                    new ResponseMessageDto(e.getMessage(), LocalDateTime.now()));
        }
    }
}
