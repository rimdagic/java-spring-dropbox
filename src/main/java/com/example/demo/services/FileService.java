package com.example.demo.services;

import com.example.demo.dto.CreateFileDto;
import com.example.demo.exceptions.MissingFolderException;
import com.example.demo.models.Account;
import com.example.demo.models.File;
import com.example.demo.models.Folder;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.FileRepository;
import com.example.demo.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    FileRepository fileRepository;
    FolderRepository folderRepository;
    AccountRepository accountRepository;

    @Autowired
    public FileService(FileRepository fileRepository, FolderRepository folderRepository, AccountRepository accountRepository){
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.accountRepository = accountRepository;

    }

    public File saveFile(MultipartFile multipartFile, String folderName, String username) throws IOException {

        if(userHasFolder(username, folderName)){
            Optional<Folder> folder = folderRepository.findByName(folderName);
            UUID folderId = folder.get().getId();

            CreateFileDto createFileDto = new CreateFileDto(multipartFile.getOriginalFilename(), multipartFile.getBytes(), folderId);
            return fileRepository.save(new File(createFileDto));
        }
        else throw new MissingFolderException("Signed in user " + username + " does not have the folder " + folderName);
    }

    public boolean userHasFolder(String username, String folderName){
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        List<Folder> usersFolders = folderRepository.findByOwnerId(accountOptional.get().getId());

        return usersFolders.stream()
                .anyMatch(folder -> folder.getName().equals(folderName));
    }

    public File loadFileAsBytes(String fileName){
        File file = fileRepository.findByName(fileName);
        System.out.println(file);
        return file;
    }
}
