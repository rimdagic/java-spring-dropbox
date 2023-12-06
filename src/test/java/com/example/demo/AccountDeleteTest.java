package com.example.demo;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //stäng av säkerheten
@TestPropertySource("classpath:application-test.properties")
public class AccountDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private String username = "anvandarnam";
    private String password = "pasword";

    private CreateAccountDto accountDto = new CreateAccountDto();
    private Account testAccount;


    @BeforeEach
    void setUp(){
        accountDto.setUsername(username);
        accountDto.setPassword(password);
    }


    @Test
    void deleteAccountTest() throws Exception {

        //Given
        testAccount = accountRepository.save(new Account(accountDto));

        //When
        mockMvc.perform(delete("/account/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        //Then
        Optional<Account> deletedAccountOptional = accountRepository.findByUsername(username);
        Account deletedAccount = null;

        if(deletedAccountOptional.isPresent()){
            deletedAccount = deletedAccountOptional.get();
        }

        assertNull(deletedAccount);
    }

    @AfterEach
    void cleanUp(){
        var account = accountRepository.findByUsername(username);

        if(account.isPresent()){
            accountRepository.deleteByUsername(account.get().getUsername());
        }
    }

}
