package com.example.demo.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dto.CreateAccountDto;
import com.example.demo.dto.ResponseMessageDto;
import com.example.demo.models.Account;
import com.example.demo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
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
    public ResponseEntity<Object> register(@RequestBody CreateAccountDto createAccountDto){

        try {
            var result = accountService.createAccount(createAccountDto);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(
                    new ResponseMessageDto(
                            e.getMessage(),
                            LocalDateTime.now()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll(){
        var result = accountService.getAllAccounts();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public String login(@RequestHeader String username, @RequestHeader String password) {
        try {
            var auth = new UsernamePasswordAuthenticationToken(username, password);
            var result = authenticationProvider.authenticate(auth);

            if (result.isAuthenticated()) {
                var algorithm = Algorithm.HMAC256("keyboardcat");
                var token = JWT.create()
                        .withSubject(username)
                        .withIssuer("auth0")
                        .sign(algorithm);

                System.out.println(username + " logged in at " + new Date());
                return token;
            } else {
                System.out.println("Authentication failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed to authenticate";
    }
}
