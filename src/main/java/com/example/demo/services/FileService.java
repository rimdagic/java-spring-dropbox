package com.example.demo.services;

import com.example.demo.dto.CreateFileDto;
import com.example.demo.exceptions.FileAlreadyExistsException;
import com.example.demo.exceptions.FileSizeTooLargeException;
import com.example.demo.exceptions.MissingFileException;
import com.example.demo.exceptions.MissingFolderException;
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

/**
 * The `FileService` class handles the logic related to the file entities. It is used between the controller that
 * that handles request and response communication, and the repository that handles the database storage operations.
 */
@Service
public class FileService {

    FileRepository fileRepository;
    FolderService folderService;
    FolderRepository folderRepository;
    AccountRepository accountRepository;

    /**
     * Constructor of the `FileService` class.
     *
     * @param fileRepository    Used to access the database table containing files.
     * @param folderRepository  Used to access the database table containing folders.
     * @param accountRepository Used to access the database table containing accounts.
     * @param folderService     Used to access the logic concerning folders.
     */
    @Autowired
    public FileService(FileRepository fileRepository, FolderRepository folderRepository, AccountRepository accountRepository, FolderService folderService) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.accountRepository = accountRepository;
        this.folderService = folderService;
    }

    /**
     * The `saveFile` method is used to decide if a file should be saved and if it is possible before it is saved in
     * the file repository.
     *
     * @param multipartFile The file data of the file that is supposed to be stored.
     * @param folderName    The folder name where the file is supposed to be stored.
     * @param username      The Username of the account that attempts to store the file.
     * @return The file that was saved is returned if it is successful.
     * @throws IOException                If unable to get bytes from multipart file
     * @throws FileSizeTooLargeException  If the file size exceeds the allowed limit.
     * @throws FileAlreadyExistsException If a file with the same name already exists in the specified folder.
     * @throws MissingFolderException     If the specified folder does not exist for the given user.
     */
    public File saveFile(MultipartFile multipartFile, String folderName, String username) throws IOException {

        if (!sizeTooLarge(multipartFile)) {
            Optional<Folder> specificFolderOptional = folderService.getUsersFolder(username, folderName);

            if (specificFolderOptional.isPresent()) {
                UUID folderId = specificFolderOptional.get().getId();
                String filename = multipartFile.getOriginalFilename();
                Optional<File> fileInFolder = getFileFromFolder(filename, folderId);

                if (fileInFolder.isEmpty()) {
                    CreateFileDto createFileDto = new CreateFileDto(multipartFile.getOriginalFilename(), multipartFile.getBytes(), folderId);
                    return fileRepository.save(new File(createFileDto));
                } else
                    throw new FileAlreadyExistsException("User " + username + " already has the file " + filename + " in folder " + folderName);
            } else
                throw new MissingFolderException("Signed in user " + username + " does not have the folder " + folderName);
        } else
            throw new FileSizeTooLargeException("File size is too large");
    }

    /**
     * Method `downloadFile` is used to evaluate if a user can download the specified file from the specified folder and
     * returns the file if successful.
     *
     * @param username   Username of the account that attempt to download file.
     * @param folderName The folder name specified where the file are to be found.
     * @param filename   The specified filename that is attempted to download.
     * @return The file is returned if the evaluation and retrieving process is successful.
     * @throws MissingFileException   If a file with the specified filename is not found
     * @throws MissingFolderException If the account does not own a folder with the specified folder name.
     */
    public File downloadFile(String username, String folderName, String filename) {
        Optional<Folder> folder = folderService.getUsersFolder(username, folderName);
        if (folder.isPresent()) {
            var fileOptional = getFileFromFolder(filename, folder.get().getId());
            if (fileOptional.isPresent()) {
                return fileOptional.get();
            } else throw new MissingFileException("File does not exist");

        } else throw new MissingFolderException("Folder does not exist");
    }

    /**
     * The `getFileFromFolder` method is used as a help method used by the `downloadFile` and `saveFile` methods.
     * It returns an optional with a file with a specified filename from a folder with a specified id.
     *
     * @param filename The name of the file that is supposed to be returned.
     * @param folderId The unique id of the folder that is supposed to contain the file.
     * @return an optional with a file if found.
     */
    public Optional<File> getFileFromFolder(String filename, UUID folderId) {
        List<File> foldersFiles = fileRepository.findFilesByFolderId(folderId);
        return foldersFiles.stream()
                .filter(file -> file.getName().equals(filename))
                .findFirst();
    }

    /**
     * The `deleteFile` method evaluates if a specific file in a specific folder owned by a specific account can be
     * deleted. If the evaluation shows that the file can be deleted, it calls the corresponding method in the file
     * repository to delete the file from the database.
     *
     * @param username   Username of the account that attempts to delete a file.
     * @param folderName The name of the folder where the file is supposed to be found.
     * @param filename   The name of the file that the user wants to delete.
     * @return The file is returned to show what file was deleted if the file is successfully deleted.
     * @throws MissingFileException   if the file is not found in the specified folder.
     * @throws MissingFolderException if the account does not own a folder with the specified folder name.
     */
    public File deleteFile(String username, String folderName, String filename) {
        Optional<Folder> folder = folderService.getUsersFolder(username, folderName);

        if (folder.isPresent()) {
            List<File> foldersFiles = fileRepository.findFilesByFolderId(folder.get().getId());

            Optional<File> chosenFileOptional = foldersFiles.stream()
                    .filter(file -> file.getName().equals(filename))
                    .findFirst();

            if (chosenFileOptional.isPresent()) {
                File chosenFile = chosenFileOptional.get();
                fileRepository.delete(chosenFile);
                return chosenFile;
            } else throw new MissingFileException("User does not have file with corresponding filename in folder");
        } else throw new MissingFolderException("User does not have that folder");
    }

    /**
     * This method is used evaluate if a file is larger than a preset limit.
     *
     * @param file the multipart file whose file size is to be evaluated.
     * @return A boolean that is true if the file size is larger than the preset limit.
     */
    public boolean sizeTooLarge(MultipartFile file) {
        var fileSizeInBytes = file.getSize();
        var maxFileSize = 1024 * 1024 * 2;
        return fileSizeInBytes > maxFileSize;
    }
}
