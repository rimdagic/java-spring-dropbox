package com.example.demo.controllers;

import com.example.demo.dto.CreateFolderDto;
import com.example.demo.models.Folder;
import com.example.demo.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/folder")
public class FolderController {

    FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService){
        this.folderService = folderService;
    }

    @GetMapping("/{folder}")
    public ResponseEntity<Optional<Folder>> getFolders(@PathVariable String folder){

        //check auth
        //return all folders belonging to logged in user

        var result = folderService.getFolder(folder);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/new")
    public Folder createFolder(@RequestBody CreateFolderDto createFolderDto){

        var result = folderService.createFolder(createFolderDto);

        return result;
    }
}
