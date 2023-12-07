package com.example.demo;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.dto.CreateFolderDto;
import com.example.demo.models.File;
import com.example.demo.models.Folder;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.FileRepository;
import com.example.demo.repositories.FolderRepository;
import com.example.demo.services.FileService;
import com.example.demo.services.FolderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * This test class tests if files can be stored in the database. Security filters has been turned off in
 * this test class, and a test version of the database is to be used with its connection-string defined in the
 * application-test.properties file.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class FileUploadTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderService folderService;

    private final String username = "keyboardcat?)HAEWSF9AHÅPFV(Ahefvuad__USERNAME";
    private final String password = "password";
    private CreateAccountDto accountDto = new CreateAccountDto();
    private CreateFolderDto folderDto = new CreateFolderDto();
    private Folder folder;
    private MockMultipartFile testFile;
    private File savedFile;

    /**
     * Sets up data and configurations for each test case. An account is created with mockMvc request with a test
     * username and password. And a folder is also created through the `folderService` with the new test account as its
     * owner.
     *
     * @throws Exception if the operation does not work as expected.
     */
    @BeforeEach
    void prepareTest() throws Exception {
        accountDto.setUsername(username);
        accountDto.setPassword(password);

        mockMvc.perform(post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(accountDto)));

        var account = accountRepository.findByUsername(username).get();

        folderDto.setName("folder");
        folderDto.setOwnerId(account.getId());

        folder = folderService.createFolder(folderDto, username);

    }

    /**
     * This tests asserts that an uploaded test file is really uploaded through the `fileService` layer and stored in
     * the `fileRepository`. The test asserts this by saving the file directly to through the `fileService`, and then
     * retrieving the file with the same id from `fileRepository` and comparing the content and the filename of the two.
     *
     * @throws Exception if the operation does not work as expected.
     */
    @Test
    void fileUploadTest() throws Exception {

        // Given
        testFile = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

        //When
        savedFile = fileService.saveFile(testFile, folder.getName(), username);
        Optional<File> fileOptional = fileRepository.findById(savedFile.getId());

        //Then
        if (fileOptional.isPresent()) {
            File file = fileOptional.get();

            assertEquals(testFile.getOriginalFilename(), file.getName());
            assertArrayEquals(testFile.getBytes(), file.getData());
        }
    }

    /**
     * This method is run after each of the tests in this class. It cleans up the database by deleting the test file,
     * its test folder, and the test account by directly calling the `folderRepository`, the `accountService` and the
     * `fileRepository` corresponding deletion methods.
     */
    @AfterEach
    void cleanUp() {
        fileRepository.deleteById(savedFile.getId());
        folderRepository.delete(folder);
        accountRepository.deleteByUsername(username);
    }

}
