package com.example.demo.services;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UserService implements UserDetailsService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService (AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createAccount(CreateAccountDto createAccountDto) {

        String password = createAccountDto.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);
        createAccountDto.setPassword(encryptedPassword);

        var result = accountRepository.save(new Account(createAccountDto));
        return result;
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var result = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user '" + username + "'."));

        return (UserDetails) result;
    }
}
