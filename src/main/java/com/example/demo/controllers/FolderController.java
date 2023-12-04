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

@RestController
@RequestMapping("/folder")
public class FolderController {

    FolderService folderService;
    UserService userService;

    @Autowired
    public FolderController(FolderService folderService, UserService userService){
        this.folderService = folderService;
        this.userService = userService;
    }

    @GetMapping("/{folder}")
    public ResponseEntity<Object> getFolders(
            @PathVariable String folder,
            @RequestHeader("Authorization") String authorizationHeader){
        var username = userService.getUsernameFromToken(authorizationHeader);

        try {
            var result = folderService.getFolderContent(folder, username);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.status(404).body(
                    new ResponseMessageDto(e.getMessage(), LocalDateTime.now()));
        }
    }

    @PostMapping("/{folderName}")
    public ResponseEntity<Object> createFolder(
            @PathVariable String folderName,
            @RequestHeader("Authorization") String authorizationHeader){

        String username = userService.getUsernameFromToken(authorizationHeader);
        CreateFolderDto createFolderDto = new CreateFolderDto();
        createFolderDto.setName(folderName);

        try {
            var result = folderService.createFolder(createFolderDto, username);
            return ResponseEntity.ok().body(result);
        } catch (Exception e){
            return ResponseEntity.status(409).body(
                    new ResponseMessageDto(e.getMessage(), LocalDateTime.now()));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<Folder>> getAll(
            @RequestHeader("Authorization") String authorizationHeader){
        var username = userService.getUsernameFromToken(authorizationHeader);
        var result = folderService.getAllFoldersByUser(username);
        return ResponseEntity.ok(result);
    }
}
