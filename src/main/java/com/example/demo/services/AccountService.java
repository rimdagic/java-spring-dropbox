package com.example.demo.services;

import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public void account(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account createAccount(String username, String password) {
        var result = accountRepository.save(new Account(username, password));
        return result;
    }
}
