package com.example.demo.services;

import com.example.demo.models.File;
import com.example.demo.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class FileService {

    FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }
}
