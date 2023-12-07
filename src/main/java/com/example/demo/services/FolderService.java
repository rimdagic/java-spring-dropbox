package com.example.demo.services;

import com.example.demo.dto.CreateFolderDto;
import com.example.demo.dto.FolderContentDto;
import com.example.demo.exceptions.FolderAlreadyExistsException;
import com.example.demo.exceptions.MissingAccountException;
import com.example.demo.exceptions.MissingFolderException;
import com.example.demo.models.Account;
import com.example.demo.models.File;
import com.example.demo.models.Folder;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.FileRepository;
import com.example.demo.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The `FolderService` class is used to handle logic and evaluation concerning the folder entity between the folder
 * control layer and the folder repository layer.
 */
@Service
public class FolderService {

    FolderRepository folderRepository;
    AccountRepository accountRepository;
    FileRepository fileRepository;

    /**
     * Constructor of the `FolderService` class.
     *
     * @param folderRepository  The folder repository handles the database operations concerning the folder entity.
     * @param accountRepository The account repository handles the database operations concerning the account entity.
     * @param fileRepository    The file repository handles the database operations concerning the file entity.
     */
    @Autowired
    public FolderService(FolderRepository folderRepository, AccountRepository accountRepository, FileRepository fileRepository) {
        this.folderRepository = folderRepository;
        this.accountRepository = accountRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * The `createFolder` method evaluates if a specific account can create a folder with a specific name, and calls the
     * folder repository to do so if the evaluation is successful.
     *
     * @param createFolderDto Takes in a data transfer object representing the folder that the user wishes to create.
     * @param username        The username of the account that is attempting to create a folder.
     * @return The folder that has been created if the creation was successful.
     * @throws FolderAlreadyExistsException if a folder with the specified folder name is already associated with the
     *                                      account
     */
    public Folder createFolder(CreateFolderDto createFolderDto, String username) {
        var account = accountRepository.findByUsername(username).get();
        boolean isTaken = folderNameTaken(createFolderDto.getName(), username);

        if (!isTaken) {
            return folderRepository.save(new Folder(createFolderDto.getName(), account.getId()));
        } else
            throw new FolderAlreadyExistsException("User " + username + " already has a folder named " + createFolderDto.getName());
    }

    /**
     * A method that checks if a specific account already has a folder with a specific name associated with it.
     *
     * @param newFolderName The name of the folder to check against.
     * @param username      The username of the account to check wether it has the folder or not.
     * @return a boolean that will be true if a folder with the specified name is already associated with the
     * specified account.
     */
    public boolean folderNameTaken(String newFolderName, String username) {
        List<Folder> usersFolders = getAllFoldersByUser(username);
        return usersFolders.stream()
                .anyMatch(folder -> folder.getName().equals(newFolderName));
    }

    /**
     * A method that will is used to check what files a specific folder is containing based on username in combination
     * with folder name.
     *
     * @param folderName The name of the folder that is to be checked for content.
     * @param username   The username of the account that is the owner to the specified folder name.
     * @return folderContentDto is returned that contains the name of the folder and a list of its content.
     * @throws MissingFolderException if the account does not have a folder with the specified name associated with it.
     */
    public FolderContentDto getFolderContent(String folderName, String username) {
        Optional<Folder> specificFolderOptional = getUsersFolder(username, folderName);

        if (specificFolderOptional.isPresent()) {
            Folder specificFolder = specificFolderOptional.get();

            List<File> fileList = fileRepository.findFilesByFolderId(specificFolder.getId());
            FolderContentDto folderContentDto = new FolderContentDto(specificFolder.getName(), fileList);
            return folderContentDto;

        } else throw new MissingFolderException("User does not have that folder");
    }

    /**
     * This method is used to retrieve the folders that have a specific user as an owner.
     *
     * @param username The username of the account that owns the folders that are to be returned
     * @return A list of all folders that has the specified account as its owner.
     * @throws MissingAccountException If an account with the specified username cant be returned from the repository.
     */
    public List<Folder> getAllFoldersByUser(String username) {
        Account account;
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        boolean isAccountExisting = accountOptional.isPresent();

        if (isAccountExisting) {
            account = accountOptional.get();
            return folderRepository.findByOwnerId(account.getId());
        } else throw new MissingAccountException("Could not find account based on provided authorization JWT");
    }

    public boolean userHasFolder(String username, String folderName) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        List<Folder> usersFolders = folderRepository.findByOwnerId(accountOptional.get().getId());

        return usersFolders.stream()
                .anyMatch(folder -> folder.getName().equals(folderName));
    }

    /**
     * This method returns a specific folder that is evaluated and determined by the username of the account that owns
     * it, and the specified name of the folder. The combination of the two will result in a unique folder.
     *
     * @param username   The username of the account that owns the folder to be retrieved.
     * @param folderName The name of the folder that is to be retrieved.
     * @return An optional that contains a folder if one is found with the matching account username and folder name
     * @trows MissingAccountException if an account with the specified username isn't returned from the account
     * repository.
     */
    public Optional<Folder> getUsersFolder(String username, String folderName) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            List<Folder> usersFolders = folderRepository.findByOwnerId(accountOptional.get().getId());

            Optional<Folder> chosenFolder = usersFolders.stream()
                    .filter(folder -> folder.getName().equals(folderName))
                    .findAny();

            return chosenFolder;
        } else throw new MissingAccountException("No such user");
    }
}
