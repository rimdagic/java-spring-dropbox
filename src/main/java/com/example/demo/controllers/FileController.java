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

/**
 * The `FileController` class handles HTTP requests related to files.
 * It provides endpoints for uploading, downloading, and deleting files.
 * <p>
 * Endpoints:
 * -GET /file/{folderName}/{filename}: Download a file.
 * -POST /file/{folderName}: Upload a file to a folder.
 * -DELETE /file/{folderName}/{filename}: Deletes a specified file in users specified folder.
 */
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

    /**
     * Download a specified file from a specified folder for the authenticated user.
     * <p>
     * This endpoint handles the HTTP GET request to retrieve a file from the user's folder.
     *
     * @param folderName          The name of the folder from which the file should be retrieved.
     * @param filename            The name of the file to be retrieved and returned in a response.
     * @param authorizationHeader The authorization header containing the user's JWT-string.
     * @return A ResponseEntity with the retrieved file if successful, headers containing content type, attachment and
     * filename and a body containing the file data with an HTTP status of 200 (OK).
     * If the file is not found, it returns a ResponseEntity with a 404 (NOT FOUND) status
     * and an error message in the response body.
     * @throws IOException If an I/O error occurs during the file retrieving process.
     */
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

    /**
     * Saves a file to a specified folder for the authenticated user.
     * <p>
     * This endpoint handles an HTTP POST request to save a file to the user's folder.
     *
     * @param multipartFile       The file to be saved, sent as a part of the request.
     * @param folderName          The name of the folder where the file should be saved.
     * @param authorizationHeader The authorization header containing the user's JWT-string.
     * @return A ResponseEntity with information about the saved file if successful, with an HTTP status of 200 (OK).
     * If an error occurs, it returns a ResponseEntity with a 500 (INTERNAL SERVER ERROR)
     * status and an error message in the response body.
     * @throws IOException If an I/O error occurs while file is being saved to the database.
     */
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
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    new ResponseMessageDto(e.getMessage(), LocalDateTime.now()));
        }
    }

    /**
     * Deletes a specified file from a specified folder for the authenticated user.
     * <p>
     * This endpoint handles the HTTP DELETE request to delete a file from the user's folder.
     *
     * @param folderName          The name of the folder from which the file should be deleted.
     * @param filename            The name of the file to be deleted.
     * @param authorizationHeader The authorization header containing the user's JWT-string.
     * @return A ResponseEntity with the deleted file information if successful, with an HTTP status of 200 (OK).
     * If the file or folder is not found, it returns a ResponseEntity with a 404 (NOT FOUND) status
     * and an error message in the response body.
     * @throws IOException If an I/O error occurs during the file deletion process.
     */
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
