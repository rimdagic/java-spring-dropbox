package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dto.CreateAccountDto;
import com.example.demo.exceptions.MissingAccountException;
import com.example.demo.exceptions.RegistrationFailedException;
import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The `AccountService` class handles the logic concerning the user Accounts. It is annotated as a Service and does also
 * have a constructor that takes all arguments.
 */
//@AllArgsConstructor
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
            throw new RegistrationFailedException("Was not able to register new account with username " +createAccountDto.getUsername() + " message: " + e.getMessage());
        }
    }

    public String login(String username){
        var algorithm = Algorithm.HMAC256("keyboardcat");
        var token = JWT.create()
                .withSubject(username)
                .withIssuer("auth0")
                .sign(algorithm);
        return token;
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Account deleteAccount(String username){
        Optional<Account> accountOptional = accountRepository.findByUsername(username);

        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            accountRepository.delete(account);

            return account;
        }
        throw new MissingAccountException("The account that you are trying to delete does not exist");
    }
}
