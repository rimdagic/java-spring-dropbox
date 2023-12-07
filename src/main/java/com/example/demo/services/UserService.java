package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
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

    public static String getUsernameFromToken(String jwtToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            return decodedJWT.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

}