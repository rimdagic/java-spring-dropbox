package com.example.demo;

import com.example.demo.dto.CreateAccountDto;
import com.example.demo.dto.CreateFolderDto;
import com.example.demo.models.File;
import com.example.demo.models.Folder;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.FolderRepository;
import com.example.demo.services.AccountService;
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

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
public class FileUploadTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileService fileService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderService folderService;

    @Autowired
    private AccountService accountService;

    private final String username = "keyboardcat?)HAEWSF9AHÅPFV(Ahefvuad__USERNAME";
    private final String password = "password";
    private CreateAccountDto accountDto = new CreateAccountDto();
    private CreateFolderDto folderDto = new CreateFolderDto();
    private Folder folder;
    private MockMultipartFile testFile;

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

    @Test
    void fileUploadTest() throws Exception {

        // Given
        testFile = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

        //When
        File file = fileService.saveFile(testFile, folder.getName(), username);

        //Then
        assertEquals(testFile.getOriginalFilename(), file.getName());
        assertEquals(testFile.getBytes(), file.getData());
    }

    @AfterEach
    void cleanUp() throws FileNotFoundException {
       fileService.deleteFile(username, folder.getName(), testFile.getOriginalFilename());
       folderRepository.delete(folder);
       accountService.deleteAccount(username);
    }

}
