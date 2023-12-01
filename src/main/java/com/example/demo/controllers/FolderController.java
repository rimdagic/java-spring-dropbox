package com.example.demo.controllers;

import com.example.demo.dto.CreateFolderDto;
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
    public ResponseEntity<Optional<Folder>> getFolders(@PathVariable String folder){


        var result = folderService.getFolder(folder);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/new")
    public Folder createFolder(@RequestBody CreateFolderDto createFolderDto,
                               @RequestHeader("Authorization") String authorizationHeader){

        String username = userService.getUsernameFromToken(authorizationHeader);

        var result = folderService.createFolder(createFolderDto, username);
        return result;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Folder>> getAll(@RequestHeader("Authorization") String authorizationHeader){
        var username = userService.getUsernameFromToken(authorizationHeader);
        var result = folderService.getAllFoldersByUser(username);
        return ResponseEntity.ok(result);
    }
}
