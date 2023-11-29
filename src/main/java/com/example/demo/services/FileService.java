package com.example.demo.services;

import com.example.demo.dto.CreateFileDto;
import com.example.demo.models.File;
import com.example.demo.models.Folder;
import com.example.demo.repositories.FileRepository;
import com.example.demo.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    FileRepository fileRepository;
    FolderRepository folderRepository;

    @Autowired
    public FileService(FileRepository fileRepository, FolderRepository folderRepository){
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;

    }

    public File saveFile(MultipartFile multipartFile, String folderName) throws IOException {

        Optional<Folder> folder = folderRepository.findByName(folderName);
        UUID folderId = folder.get().getId();

        CreateFileDto createFileDto = new CreateFileDto(multipartFile.getOriginalFilename(), multipartFile.getBytes(), folderId);
        return fileRepository.save(new File(createFileDto));
    }
}
