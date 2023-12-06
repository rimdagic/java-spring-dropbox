package com.example.demo;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.models.Account;
import com.example.demo.repositories.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class AccountRegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private final String username = "keyboardcat?)HAEWSF9AHÅPFV(Ahefvuad__USERNAME";
    private Account newAccount = null;

    @Test
    void testRegisterAccount() throws Exception{

        //Given
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setUsername(username);
        createAccountDto.setPassword("password");

        //When
        mockMvc.perform(post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createAccountDto)))

        //Then
                .andExpect(status().isOk());

        var newAccountOptional = accountRepository.findByUsername(username);
        if(newAccountOptional.isPresent()){
            newAccount = newAccountOptional.get();
        }

        assertEquals(username, newAccount.getUsername());
    }

    @AfterEach
    void deleteTestUser() throws Exception {
        accountRepository.deleteByUsername(username);
    }

}
