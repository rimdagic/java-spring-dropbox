package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dto.CreateAccountDto;
import com.example.demo.exceptions.MissingAccountException;
import com.example.demo.exceptions.RegistrationFailedException;
import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The `AccountService` class handles the logic concerning the applications user Accounts. It is annotated as a Service.
 */
@Service
public class AccountService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;

    /**
     * Contructor for the `AccountService` that initiates the `AccountRepository` and `PasswordEncoder` classes.
     *
     * @param accountRepository Gives the service access to an instance of the AccountRepository to access the database.
     * @param passwordEncoder   The password encoder can encode passwords to store them hashed in the database.
     */
    @Autowired
    public void account(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * The `createAccount` method is used to create a new account.
     *
     * @param createAccountDto contains a username and password, and the method tries to create a new account with those
     *                         submitted credentials.
     * @return The result is returned is the newly created account object if successful. Otherwise, an exception will be
     * thrown.
     * @thrown If the account creation process fails, typically due to issues with data integrity
     * or database operations.
     */
    public Account createAccount(CreateAccountDto createAccountDto) {

        String password = createAccountDto.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);
        createAccountDto.setPassword(encryptedPassword);

        try {
            var result = accountRepository.save(new Account(createAccountDto));
            return result;
        } catch (Exception e) {
            throw new RegistrationFailedException("Was not able to register new account with username " + createAccountDto.getUsername() + " message: " + e.getMessage());
        }
    }

    /**
     * The `login` method builds a JWT based on a specific username using an algorithm with a secret to match the
     * authorization verifying process.
     *
     * @param username that will be used to generate a JWT.
     * @return a JWT that the user can use to get authorization to specific endpoints.
     */
    public String login(String username) {
        var algorithm = Algorithm.HMAC256("keyboardcat");
        var token = JWT.create()
                .withSubject(username)
                .withIssuer("auth0")
                .sign(algorithm);
        return token;
    }

    /**
     * `getAllAccounts` method returns a list of all the accounts in the database with the specified authorities.
     *
     * @return a list of all the accounts that is found in the account table in the database.
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * The `deleteAccount` method deletes an account based on the provided username.
     *
     * @param username The `username` of the account that is to be deleted.
     * @return The account that has been deleted represented as a JSON object if successful. Otherwise an exception is
     * thrown that no account with the specified username was found.
     */
    public Account deleteAccount(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            accountRepository.delete(account);

            return account;
        }
        throw new MissingAccountException("The account that you are trying to delete does not exist");
    }
}
