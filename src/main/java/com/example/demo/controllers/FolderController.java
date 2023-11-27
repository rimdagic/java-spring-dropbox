package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/folder")
public class FolderController {

    @GetMapping("/get")
    public String getAll(){
        //check auth
        //return all folders belonging to logged in user
        return "here folders";
    }

    @PostMapping("/create")
    public String create(){
        // check auth
        // create folder to user if logged in
        return "create folder";
    }
}
