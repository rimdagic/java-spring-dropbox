package com.example.demo.controllers;

import com.example.demo.dto.CreateFolderDto;
import com.example.demo.dto.ResponseMessageDto;
import com.example.demo.models.Folder;
import com.example.demo.services.FolderService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The `FolderController` class handles HTTP requests related to folders.
 * It provides endpoints for creating, listing, viewing content and deleting folders.
 * <p>
 * Endpoints:
 * -GET /folder/{folderName}: Listing the content of a specific folder.
 * -POST /folder/{folderName}: Creating a new folder named as specified in the path-variable.
 * -GET /folder/all: Get a list of all folders belonging to user.
 */
@RestController
@RequestMapping("/folder")
public class FolderController {

    FolderService folderService;
    UserService userService;

    @Autowired
    public FolderController(FolderService folderService, UserService userService) {
        this.folderService = folderService;
        this.userService = userService;
    }

    /**
     * Retrieves and return a list of files found in a specific folder.
     * <p>
     * This endpoint handles an HTTP GET request and returns a ResponseEntity with a list of files
     * that was found in the folder.
     *
     * @param folderName          The name of the folder that the user wishes to view the content of.
     * @param authorizationHeader The authorization header containing the user's JWT-string.
     * @return A ResponseEntity with a FolderContentDto object that contains the folders name and a list of
     * files found in the folder if successful, with an HTTP status of 200 (OK).
     * If an error occurs, it returns a ResponseEntity with a 404 (NOT FOUND)
     * status and an ResponseEntity containing a ResponseMessageDto with a specified
     * error message in the response body.
     */
    @GetMapping("/{folderName}")
    public ResponseEntity<Object> getFolder(
            @PathVariable String folderName,
            @RequestHeader("Authorization") String authorizationHeader) {
        var username = userService.getUsernameFromToken(authorizationHeader);

        try {
            var result = folderService.getFolderContent(folderName, username);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(
                    new ResponseMessageDto(e.getMessage(), LocalDateTime.now()));
        }
    }

    /**
     * Creates a new folder
     * <p>
     * This endpoint handles an HTTP POST request to create a new folder belonging to the authenticated user.
     *
     * @param folderName          The name of the folder that the user wishes to create.
     * @param authorizationHeader The authorization header containing the user's JWT-string.
     * @return A ResponseEntity with a JSON representation of the created Folder if successful,
     * with an HTTP status of 200 (OK).
     * If an error occurs, it returns a ResponseEntity with a 409 (CONFLICT)
     * status and an ResponseEntity containing a ResponseMessageDto with a specified
     * error message in the response body.
     */
    @PostMapping("/{folderName}")
    public ResponseEntity<Object> createFolder(
            @PathVariable String folderName,
            @RequestHeader("Authorization") String authorizationHeader) {

        String username = userService.getUsernameFromToken(authorizationHeader);
        CreateFolderDto createFolderDto = new CreateFolderDto();
        createFolderDto.setName(folderName);

        try {
            var result = folderService.createFolder(createFolderDto, username);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(409).body(
                    new ResponseMessageDto(e.getMessage(), LocalDateTime.now()));
        }
    }

    /**
     * View a list of current users all folders
     * <p>
     * This endpoint handles an HTTP GET request to retrieve a list of the authenticated users all folders.
     *
     * @param authorizationHeader The authorization header containing the user's JWT-string.
     * @return A ResponseEntity with a List of folders if successful,
     * with an HTTP status of 200 (OK). If user provides a JWT that is not valid,
     * the security filter will return an HTTP status of 403 (FORBIDDEN).
     */
    @GetMapping("/all")
    public ResponseEntity<List<Folder>> getAll(
            @RequestHeader("Authorization") String authorizationHeader) {
        var username = userService.getUsernameFromToken(authorizationHeader);
        var result = folderService.getAllFoldersByUser(username);
        return ResponseEntity.ok(result);
    }
}
