package com.example.demo.controllers;

import com.example.demo.dto.CreateFolderDto;
import com.example.demo.dto.FolderContentDto;
import com.example.demo.exceptions.FolderAlreadyExistsException;
import com.example.demo.exceptions.MissingFolderException;
import com.example.demo.models.Folder;
import com.example.demo.services.FolderService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<FolderContentDto> getFolders(
            @PathVariable String folder,
            @RequestHeader("Authorization") String authorizationHeader){

        var username = userService.getUsernameFromToken(authorizationHeader);

        var result = folderService.getFolder(folder, username);
        System.out.println(result.getFiles().toString());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{folderName}")
    public ResponseEntity<Folder> createFolder(
            @PathVariable String folderName,
            @RequestHeader("Authorization") String authorizationHeader){

        String username = userService.getUsernameFromToken(authorizationHeader);

        CreateFolderDto createFolderDto = new CreateFolderDto();
        createFolderDto.setName(folderName);

            var result = folderService.createFolder(createFolderDto, username);
            return ResponseEntity.ok().body(result);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Folder>> getAll(
            @RequestHeader("Authorization") String authorizationHeader){
        var username = userService.getUsernameFromToken(authorizationHeader);
        var result = folderService.getAllFoldersByUser(username);
        return ResponseEntity.ok(result);
    }
}
