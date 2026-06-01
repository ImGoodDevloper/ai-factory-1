package com.kes.api.controller;

import com.kes.api.dto.MediaUploadResponse;
import com.kes.api.service.MediaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MediaController.class)
@WithMockUser(roles = "ADMIN")
class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MediaService mediaService;

    @Test
    void upload_ShouldReturnUrlAndFilename() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        MediaUploadResponse response = new MediaUploadResponse("/media/uuid.png", "uuid.png");
        when(mediaService.uploadMedia(any())).thenReturn(response);

        mockMvc.perform(multipart("/api/media/upload").file(file).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("/media/uuid.png"))
                .andExpect(jsonPath("$.filename").value("uuid.png"));
    }
}
