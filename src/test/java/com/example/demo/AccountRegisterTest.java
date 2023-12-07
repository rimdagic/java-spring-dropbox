package com.example.demo;

import com.example.demo.dto.CreateAccountDto;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test class tests registering new accounts to the `AccountRepository`. Security filters has been turned off in
 * this test class, and a test version of the database is to be used with its connection-string defined in the
 * application-test.properties file.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class AccountRegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private final String username = "keyboardcat?)HAEWSF9AHÅPFV(Ahefvuad__USERNAME";

    /**
     * This test creates a `createAccountDto` and sets its username and password to valid strings. The account is then
     * sent with a mockMvc request that is to be received by the `accountController` to create a new account with the
     * defined credentials. The test then asserts that the created account is present by getting it from the
     * `accountRepository`.
     *
     * @throws Exception if the operation does not work as expected.
     */
    @Test
    void testRegisterAccount() throws Exception {

        //Given
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setUsername(username);
        createAccountDto.setPassword("password");

        //When
        mockMvc.perform(post("/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createAccountDto)))
                .andExpect(status().isOk());

        //Then
        var newAccountOptional = accountRepository.findByUsername(username);
        assertTrue(newAccountOptional.isPresent());
    }

    /**
     * This method is run after each of the tests in this class. It cleans up the database by deleting the test account
     * after the test by directly calling the `accountRepository` to delete the account that has the username that
     * corresponds to the test accounts' username.
     */
    @AfterEach
    void deleteTestUser() throws Exception {
        accountRepository.deleteByUsername(username);
    }

}
