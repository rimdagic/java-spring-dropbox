package com.example.demo.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dto.CreateAccountDto;
import com.example.demo.models.Account;
import com.example.demo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/account")
@RestController
public class AccountController {

    private AccountService accountService;
    private AuthenticationProvider authenticationProvider;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountController(AccountService accountService, AuthenticationProvider authenticationProvider, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.authenticationProvider = authenticationProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody CreateAccountDto createAccountDto){
        var result = accountService.createAccount(createAccountDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll(){
        var result = accountService.getAllAccounts();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public String login(@RequestHeader String username, @RequestHeader String password) {
        var auth = new UsernamePasswordAuthenticationToken(username, passwordEncoder.encode(password));
        var result = authenticationProvider.authenticate(auth);

        System.out.println("HÄÄÄR------------------------------------------------------------------");

        if (result.isAuthenticated()) {
            var algoritm = Algorithm.HMAC256("keyboardcatEV)+7yeVE)AV/Y345E)yvEA)7)(/&%vduHY7GEDW(/W¤Q#46578!)PWQ(FUIHdsU345v");
            var token = JWT.create()
                    .withSubject(username)
                    .withIssuer("auth0")
                    .sign(algoritm);

            return token;
        }

        return "Failed to login.";
    }
}
