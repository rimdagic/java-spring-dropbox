package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The `UserService` is a service between the controllers and the account repository specifically used for security
 * authentication and authorization purposes.
 */
public class UserService implements UserDetailsService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;

    /**
     * The `UserService` constructor that injects the account repository and the password encoder to the class.
     *
     * @param accountRepository Is used to access the user accounts.
     * @param passwordEncoder   Is used to hash passwords that are to be stored safely and to hash passwords of login
     *                          requests to match with the password in the repository.
     */
    @Autowired
    public UserService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method extracts a username from a JWT.
     *
     * @param jwtToken Contains the JWT of which the username is to be extracted.
     * @return The username that has been extracted from the token.
     */
    public static String getUsernameFromToken(String jwtToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            return decodedJWT.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * The `loadUserByUsername` method loads an account from the repository with a specified username. The builds a
     * UserDetails object based on the username password an authorities.
     *
     * @param username The username of the account to be authorized.
     * @return userDetails of the account with the specified username.
     * @throws UsernameNotFoundException if an account with the corresponding username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user '" + username + "'."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(account.getAuthorities())
                .build();
    }

}