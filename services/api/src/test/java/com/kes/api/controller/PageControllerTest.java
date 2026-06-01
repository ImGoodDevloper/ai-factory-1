package com.kes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kes.api.dto.PageCreateDto;
import com.kes.api.dto.PageDetailDto;
import com.kes.api.dto.PageSummaryDto;
import com.kes.api.dto.PageUpdateDto;
import com.kes.api.exception.ResourceNotFoundException;
import com.kes.api.service.PageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PageController.class)
@WithMockUser(roles = "ADMIN")
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageService pageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getRootPages_ShouldReturnList() throws Exception {
        PageSummaryDto summary = PageSummaryDto.builder().id(1L).title("Root").build();
        when(pageService.getRootPages()).thenReturn(List.of(summary));

        mockMvc.perform(get("/api/pages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Root"));
    }

    @Test
    void getPageDetail_WhenExists_ShouldReturnDetail() throws Exception {
        PageDetailDto detail = PageDetailDto.builder().id(1L).title("Detail").build();
        when(pageService.getPageDetail(1L)).thenReturn(detail);

        mockMvc.perform(get("/api/pages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Detail"));
    }

    @Test
    void getPageDetail_WhenNotFound_ShouldReturn404() throws Exception {
        when(pageService.getPageDetail(1L)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/pages/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Not found"))
                .andExpect(jsonPath("$.type").value("https://kes.com/errors/not-found"));
    }

    @Test
    void search_ShouldReturnMatchingPages() throws Exception {
        PageSummaryDto summary = PageSummaryDto.builder().id(1L).title("Search Result").build();
        when(pageService.searchPages("test")).thenReturn(List.of(summary));

        mockMvc.perform(get("/api/search").param("q", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Search Result"));
    }

    @Test
    void createPage_WithValidData_ShouldReturn201() throws Exception {
        PageCreateDto createDto = PageCreateDto.builder().title("New Page").build();
        PageDetailDto detail = PageDetailDto.builder().id(1L).title("New Page").build();
        when(pageService.createPage(any(PageCreateDto.class))).thenReturn(detail);

        mockMvc.perform(post("/api/pages")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Page"));
    }

    @Test
    void updatePage_ShouldReturnUpdatedDetail() throws Exception {
        PageUpdateDto updateDto = PageUpdateDto.builder().title("Updated").build();
        PageDetailDto detail = PageDetailDto.builder().id(1L).title("Updated").build();
        when(pageService.updatePage(eq(1L), any(PageUpdateDto.class))).thenReturn(detail);

        mockMvc.perform(put("/api/pages/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void deletePage_WhenSuccessful_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/pages/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePage_WhenLocked_ShouldReturn400() throws Exception {
        doThrow(new IllegalStateException("Cannot delete a locked page.")).when(pageService).deletePage(1L);

        mockMvc.perform(delete("/api/pages/1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid State"))
                .andExpect(jsonPath("$.detail").value("Cannot delete a locked page."));
    }

    @Test
    void getPageDetail_WhenBusinessException_ShouldReturn400() throws Exception {
        when(pageService.getPageDetail(1L)).thenThrow(new com.kes.api.exception.BusinessException("Business error"));

        mockMvc.perform(get("/api/pages/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Business Rule Violation"))
                .andExpect(jsonPath("$.detail").value("Business error"));
    }

    @Test
    void getPageDetail_WhenGeneralException_ShouldReturn500() throws Exception {
        when(pageService.getPageDetail(1L)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/pages/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.trace_id").exists());
    }
}
