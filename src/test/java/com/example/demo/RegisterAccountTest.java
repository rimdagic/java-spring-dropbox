package com.example.demo;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterAccountTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    PasswordEncoder passwordEncoder;


    @Test
    void testRegisterAccount() throws Exception{
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setUsername("test");
        createAccountDto.setPassword("test");

        mockMvc.perform(post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createAccountDto)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).createAccount(eq(createAccountDto));
    }

}
