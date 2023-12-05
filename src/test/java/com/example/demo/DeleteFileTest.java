package com.example.demo;

import com.example.demo.dto.CreateFileDto;
import com.example.demo.models.File;
import com.example.demo.services.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DeleteFileTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FileService fileService;


    @Test
    void testDeleteFile() throws Exception {

        String username = "testuser";
        String folderName = "testfolder";
        String filename = "test.txt";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlzcyI6ImF1dGgwIn0.m_kN1_zE6PYttoChEcJXKoJk88sMHSs91x8nhWAAlxQ";

        when(fileService.deleteFile(eq(username), eq(folderName), eq(filename)))
                .thenReturn(new File(new CreateFileDto("name", "content".getBytes(), UUID.fromString("54550058-838c-48e4-8c9c-d90d6b5a8d6b"))));

        mockMvc.perform(delete("/file/{folderName}/{filename}", folderName, filename)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }
}