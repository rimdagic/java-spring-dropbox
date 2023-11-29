package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/{folder}")
public class FileController {

    @GetMapping("/{file}")
    public String getFile(@PathVariable String folder, @PathVariable("file") String fileName) {

        return "File: " + fileName + " in folder: " + folder;
    }
}
