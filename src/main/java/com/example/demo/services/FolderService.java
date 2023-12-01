package com.example.demo.services;

import com.example.demo.dto.CreateFolderDto;
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
        var accountOptional = accountRepository.findByUsername(username);
        var folderOptional = folderRepository.findByName(createFolderDto.getName());

        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            return folderRepository.save(new Folder(createFolderDto.getName(), account.getId()));
        } else return null;
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
        } else throw new MissingAccountException("Could not find account based on JWT Token");
    }
}
