package com.example.demo.controllers;

import com.example.demo.dto.CreateAccount;
import com.example.demo.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateAccount createAccount){
        accountService.createAccount(createAccount.getUsername(), createAccount.getPassword());
        return ResponseEntity.ok("success");
    }
}
