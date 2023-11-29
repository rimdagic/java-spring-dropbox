package com.example.demo.controllers;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.models.Account;
import com.example.demo.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody CreateAccountDto createAccountDto){
        var result = accountService.createAccount(createAccountDto);
        return ResponseEntity.ok(result);
    }
}
