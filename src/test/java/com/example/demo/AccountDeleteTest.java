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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test class tests deletion of accounts from the `AccountRepository`. Security filters has been turned off in
 * this test class, and a test version of the database is to be used with its connection-string defined in the
 * application-test.properties file.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class AccountDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private final String username = "username";
    private final String password = "password";

    private CreateAccountDto accountDto = new CreateAccountDto();
    private Account testAccount;

    /**
     * Sets up data and configurations for each test case.
     */
    @BeforeEach
    void setUp() {
        accountDto.setUsername(username);
        accountDto.setPassword(password);
    }

    /**
     * This test tests if an account can be deleted from the `accountRepository`. A test account is saved to the
     * `account repository`, then a mockMVC DELETE request is sent to a URL that is to be received by the
     * `accountController`. Then the test asserts that the account is no longer present by trying to find the specified
     * test account directly from the `accountRepository` and checking that it is in fact not there anymore.
     *
     * @throws Exception if the operation does not work as expected.
     */
    @Test
    void deleteAccountTest() throws Exception {

        //Given
        testAccount = accountRepository.save(new Account(accountDto));

        //When
        mockMvc.perform(delete("/account/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        //Then
        Optional<Account> deletedAccountOptional = accountRepository.findByUsername(username);
        assertFalse(deletedAccountOptional.isPresent());

    }

    /**
     * This method is run after each of the tests in this class. It cleans up the database by deleting the test account
     * if it is still present after the test. The account is then deleted by directly calling the `accountRepository`
     * to delete the account that has the username that corresponds to the test accounts' username.
     */
    @AfterEach
    void cleanUp() {
        var account = accountRepository.findByUsername(username);

        if (account.isPresent()) {
            accountRepository.deleteByUsername(account.get().getUsername());
        }
    }

}
