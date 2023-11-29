package com.example.demo.services;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public void account(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account createAccount(CreateAccountDto createAccountDto) {
        var result = accountRepository.save(new Account(createAccountDto));
        return result;
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }
}
