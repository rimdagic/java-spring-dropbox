package com.example.demo.controllers;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.dto.LoginAccountDto;
import com.example.demo.dto.ResponseMessageDto;
import com.example.demo.exceptions.MissingAccountException;
import com.example.demo.exceptions.RegistrationFailedException;
import com.example.demo.models.Account;
import com.example.demo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * The `Account controller` class represents a controller that handles HTTP-requests
 * about the applications user accounts with appropriate responses.
 * It provides methods to register a new account, list all accounts, log in to account and delete an account.
 * <p>
 * Endpoints:
 * -POST /account/register: Register a new account.
 * -GET /account/all: List all accounts.
 * -POST /account/login: Log in to an account.
 * -DELETE /account/{username}
 */
@RequestMapping("/account")
@RestController
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public AccountController(AccountService accountService, AuthenticationProvider authenticationProvider, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Registers a new user.
     * <p>
     * This endpoint handles the HTTP POST request for registering a new account.
     *
     * @return A ResponseEntity containing the new account if successful, with an HTTP status of 200 (OK)
     * and an error message with an HTTP status of 409 (CONFLICT) if the account already exists.
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody CreateAccountDto createAccountDto) {

        try {
            var result = accountService.createAccount(createAccountDto);
            logger.info("A new account with username " + createAccountDto.getUsername() + " was created at " + new Date());
            return ResponseEntity.ok(result);
        } catch (RegistrationFailedException e) {
            return ResponseEntity.status(409).body(
                    new ResponseMessageDto(
                            e.getMessage(),
                            LocalDateTime.now()));
        }
    }

    /**
     * Retrieves a list of all accounts.
     * <p>
     * This endpoint handles the HTTP GET request for fetching all accounts.
     *
     * @return A ResponseEntity containing a list of accounts if successful, with an HTTP status of 200 (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll() {
        var result = accountService.getAllAccounts();
        return ResponseEntity.ok(result);
    }

    /**
     * Logs in user.
     * <p>
     * This endpoint handles the HTTP POST request for fetching an authentication token.
     *
     * @return A ResponseEntity containing a JWT if successful, with an HTTP status of 200 (OK).
     * and an error message with an HTTP status of 401 (UNAUTHORIZED) if not successful.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginAccountDto loginAccountDto) {
        var username = loginAccountDto.getUsername();
        var password = loginAccountDto.getPassword();

        try {
            var auth = new UsernamePasswordAuthenticationToken(username, password);
            var result = authenticationProvider.authenticate(auth);

            if (result.isAuthenticated()) {
                var token = accountService.login(username);
                logger.info(username + " logged in at " + new Date());

                return ResponseEntity.ok(token);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to authenticate");
    }

    /**
     * Deletes user account.
     * <p>
     * This endpoint handles the HTTP DELETE request for deleting a user account.
     *
     * @return A ResponseEntity containing the deleted user account if successful, with an HTTP status of 200 (OK).
     * and an error message with an HTTP status of 404 (NOT FOUND) if not successful.
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteAccount(@PathVariable String username) {
        try {
            Account account = accountService.deleteAccount(username);
            return ResponseEntity.ok(account);
        } catch (MissingAccountException e) {
            return ResponseEntity.status(404).body(new ResponseMessageDto(e.getMessage(), LocalDateTime.now()));
        }
    }
}
