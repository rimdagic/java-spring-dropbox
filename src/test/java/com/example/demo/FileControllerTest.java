package com.example.demo;

import com.example.demo.dto.CreateFileDto;
import com.example.demo.models.File;
import com.example.demo.services.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.util.Base64;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    void testSaveFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

        String username = "testuser";
        String folderName = "testfolder";
        String authorizationHeader = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlzcyI6ImF1dGgwIn0.m_kN1_zE6PYttoChEcJXKoJk88sMHSs91x8nhWAAlxQ";

        UUID mockFolderId = Mockito.mock(UUID.class);
        Mockito.when(mockFolderId.toString()).thenReturn("54550058-838c-48e4-8c9c-d90d6b5a8d6b");

        when(fileService.saveFile(any(), any(), any()))
                .thenReturn(new File(new CreateFileDto(file.getName(), file.getBytes(), mockFolderId)));

        mockMvc.perform(multipart("/file/{folderName}", folderName)
                        .file(file)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(file.getName()))
                .andExpect(content().json(String.format("{\"data\":\"%s\"}", Base64.getEncoder().encodeToString(file.getBytes()))));

        verify(fileService, times(1)).saveFile(eq(file), eq(folderName), eq(username));
    }
}
