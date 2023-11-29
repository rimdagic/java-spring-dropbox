package com.example.demo.services;

import com.example.demo.dto.CreateFolderDto;
import com.example.demo.models.Folder;
import com.example.demo.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FolderService {

    FolderRepository folderRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository){
        this.folderRepository = folderRepository;
    }

    public Folder createFolder(CreateFolderDto createFolderDto){
        return folderRepository.save(new Folder(createFolderDto.getName(), createFolderDto.getOwnerId()));
    }

    public Optional<Folder> getFolder(String name){
        return folderRepository.findByName(name);
    }
}
