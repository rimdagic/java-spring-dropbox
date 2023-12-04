package com.example.demo.services;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.exceptions.RegistrationFailedException;
import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AccountService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public void account(AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createAccount(CreateAccountDto createAccountDto) {

        String password = createAccountDto.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);
        createAccountDto.setPassword(encryptedPassword);

        try {
            var result = accountRepository.save(new Account(createAccountDto));
            return result;
        } catch (Exception e) {
            throw new RegistrationFailedException(e.getMessage());
        }
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }
}
