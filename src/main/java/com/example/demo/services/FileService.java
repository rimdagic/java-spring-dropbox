package com.example.demo.services;

import com.example.demo.dto.CreateFileDto;
import com.example.demo.exceptions.MissingFolderException;
import com.example.demo.models.File;
import com.example.demo.models.Folder;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.FileRepository;
import com.example.demo.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    FileRepository fileRepository;
    FolderService folderService;
    FolderRepository folderRepository;
    AccountRepository accountRepository;

    @Autowired
    public FileService(FileRepository fileRepository, FolderRepository folderRepository, AccountRepository accountRepository, FolderService folderService){
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.accountRepository = accountRepository;
        this.folderService = folderService;

    }

    public File saveFile(MultipartFile multipartFile, String folderName, String username) throws IOException {

        if(folderService.userHasFolder(username, folderName)){
            Optional<Folder> folder = folderRepository.findByName(folderName);
            UUID folderId = folder.get().getId();
            String filename = multipartFile.getOriginalFilename();
            Optional<File> fileInFolder = folderContainsFileCheck(folderId, filename);

            if(fileInFolder.isEmpty()) {
                CreateFileDto createFileDto = new CreateFileDto(multipartFile.getOriginalFilename(), multipartFile.getBytes(), folderId);
                return fileRepository.save(new File(createFileDto));
            } else throw new FileAlreadyExistsException("User " + username + " already has the file " + filename + " in folder " + folderName);
        } else throw new MissingFolderException("Signed in user " + username + " does not have the folder " + folderName);
    }


    public Optional<File> getSpecificFile(UUID id){
        Optional<File> file = fileRepository.findById(id);
        return file;
    }

    public List<File> getFilesByFolderId(UUID folderId){
        return fileRepository.findFilesByFolderId(folderId);
    }

    public Optional<File> downloadFile(String username, String folderName, String filename){
        Optional<Folder> folder = folderService.getUsersFolder(username, folderName);
        if (folder.isPresent()) {
            return getFileFromFolder(filename, folder.get().getId());
        } else throw new MissingFolderException("Folder does not exist");
    }

    public Optional<File> getFileFromFolder(String filename, UUID folderId){
        List<File> foldersFiles = fileRepository.findFilesByFolderId(folderId);
        return foldersFiles.stream()
                .filter(file -> file.getName().equals(filename))
                .findFirst();
    }


    public Optional<File> folderContainsFileCheck(UUID folderId, String filename){
        List<File> folderContent = fileRepository.findFilesByFolderId(folderId);

        return folderContent.stream()
                .filter(file -> file.getName().equals(filename))
                .findAny();
    }



    public File deleteFile(String username, String folderName, String filename) throws FileNotFoundException {
        Optional<Folder> folder = folderService.getUsersFolder(username, folderName);

        if(folder.isPresent()){
        List<File> foldersFiles = fileRepository.findFilesByFolderId(folder.get().getId());

        Optional<File> chosenFileOptional = foldersFiles.stream()
                .filter(file -> file.getName().equals(filename))
                .findFirst();

        if(chosenFileOptional.isPresent()){
            File chosenFile = chosenFileOptional.get();
            fileRepository.delete(chosenFile);
            return chosenFile;
        }
        else throw new FileNotFoundException("User does not have file with corresponding filename in folder");
    } else throw new MissingFolderException("User does not have that folder");

    }
}

