package com.example.demo.services;

import com.example.demo.dto.CreateFolderDto;
import com.example.demo.exceptions.FolderAlreadyExistsException;
import com.example.demo.exceptions.MissingAccountException;
import com.example.demo.models.Account;
import com.example.demo.models.Folder;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FolderService {

    FolderRepository folderRepository;
    AccountRepository accountRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository, AccountRepository accountRepository){
        this.folderRepository = folderRepository;
        this.accountRepository = accountRepository;
    }

    public Folder createFolder(CreateFolderDto createFolderDto, String username){
        var account = accountRepository.findByUsername(username).get();
        var folderOptional = folderRepository.findByName(createFolderDto.getName());
        boolean isTaken = folderNameTaken(createFolderDto.getName(), username);

        if(!isTaken) {
            return folderRepository.save(new Folder(createFolderDto.getName(), account.getId()));
        } else throw new FolderAlreadyExistsException("User " + username + " already has a folder named " + createFolderDto.getName());
    }

    public boolean folderNameTaken(String newFolderName, String username){

        List<Folder> usersFolders = getAllFoldersByUser(username);
        return usersFolders.stream()
                .anyMatch(folder -> folder.getName().equals(newFolderName));
    }

    public Optional<Folder> getFolder(String name){
        return folderRepository.findByName(name);
    }

    public List<Folder> getAllFoldersByUser(String username){
        Account account;
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        boolean isAccountExisting = accountOptional.isPresent();

        if(isAccountExisting){
            account = accountOptional.get();
            return folderRepository.findByOwnerId(account.getId());
        } else throw new MissingAccountException("Could not find account based on provided authorization JWT");
    }

    public boolean userHasFolder(String username, String folderName){
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        List<Folder> usersFolders = folderRepository.findByOwnerId(accountOptional.get().getId());

        return usersFolders.stream()
                .anyMatch(folder -> folder.getName().equals(folderName));
    }


    public Folder getUsersFolder(String username, String folderName){
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if(accountOptional.isPresent()) {
            List<Folder> usersFolders = folderRepository.findByOwnerId(accountOptional.get().getId());

            Folder chosenFolder = usersFolders.stream()
                    .filter(folder -> folder.getName().equals(folderName))
                    .findAny()
                    .orElse(null);
            return chosenFolder;
        }
        else throw new MissingAccountException("No such user");
    }
}
