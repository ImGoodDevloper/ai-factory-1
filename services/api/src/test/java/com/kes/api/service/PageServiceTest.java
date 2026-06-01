package com.kes.api.service;

import com.kes.api.dto.PageCreateDto;
import com.kes.api.dto.PageDetailDto;
import com.kes.api.dto.PageSummaryDto;
import com.kes.api.dto.PageUpdateDto;
import com.kes.api.entity.Page;
import com.kes.api.exception.ResourceNotFoundException;
import com.kes.api.repository.PageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PageServiceTest {

    @Mock
    private PageRepository pageRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private PageService pageService;

    private Page rootPage;
    private Page childPage;

    @BeforeEach
    void setUp() {
        rootPage = Page.builder()
                .id(1L)
                .title("Root Page")
                .content("Root Content")
                .isLocked(false)
                .children(new ArrayList<>())
                .build();

        childPage = Page.builder()
                .id(2L)
                .title("Child Page")
                .content("Child Content")
                .isLocked(false)
                .children(new ArrayList<>())
                .parent(rootPage)
                .build();
        
        rootPage.getChildren().add(childPage);
    }

    @Test
    void getRootPages_ShouldReturnRootPages() {
        when(pageRepository.findByParentIsNull()).thenReturn(List.of(rootPage));

        List<PageSummaryDto> result = pageService.getRootPages();

        assertEquals(1, result.size());
        assertEquals("Root Page", result.get(0).getTitle());
        assertTrue(result.get(0).getHasChildren());
        verify(pageRepository).findByParentIsNull();
    }

    @Test
    void getPageDetail_WhenPageExists_ShouldReturnDetail() {
        when(pageRepository.findById(1L)).thenReturn(Optional.of(rootPage));

        PageDetailDto result = pageService.getPageDetail(1L);

        assertEquals("Root Page", result.getTitle());
        assertEquals(1, result.getChildren().size());
        verify(pageRepository).findById(1L);
    }

    @Test
    void getPageDetail_WhenPageDoesNotExist_ShouldThrowException() {
        when(pageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pageService.getPageDetail(1L));
    }

    @Test
    void createPage_WithoutParent_ShouldCreateRootPage() {
        PageCreateDto createDto = PageCreateDto.builder().title("New Page").build();
        Page savedPage = Page.builder().id(3L).title("New Page").isLocked(false).children(new ArrayList<>()).build();
        
        when(pageRepository.save(any(Page.class))).thenReturn(savedPage);

        PageDetailDto result = pageService.createPage(createDto);

        assertEquals("New Page", result.getTitle());
        assertNull(result.getParentId());
        verify(pageRepository).save(any(Page.class));
    }

    @Test
    void createPage_WithParent_ShouldCreateChildPage() {
        PageCreateDto createDto = PageCreateDto.builder().title("New Child").parentId(1L).build();
        when(pageRepository.findById(1L)).thenReturn(Optional.of(rootPage));
        
        Page savedPage = Page.builder().id(3L).title("New Child").parent(rootPage).isLocked(false).children(new ArrayList<>()).build();
        when(pageRepository.save(any(Page.class))).thenReturn(savedPage);

        PageDetailDto result = pageService.createPage(createDto);

        assertEquals("New Child", result.getTitle());
        assertEquals(1L, result.getParentId());
        verify(pageRepository).findById(1L);
        verify(pageRepository).save(any(Page.class));
    }

    @Test
    void updatePage_PartialUpdate_ShouldOnlyUpdateProvidedFields() {
        PageUpdateDto updateDto = PageUpdateDto.builder().title("Updated Title").build();
        when(pageRepository.findById(1L)).thenReturn(Optional.of(rootPage));

        PageDetailDto result = pageService.updatePage(1L, updateDto);

        assertEquals("Updated Title", result.getTitle());
        assertEquals("Root Content", result.getContent()); // Should remain unchanged
        assertFalse(result.getIsLocked()); // Should remain unchanged
    }

    @Test
    void updatePage_FullUpdate_ShouldUpdateAllFields() {
        PageUpdateDto updateDto = PageUpdateDto.builder()
                .title("New Title")
                .content("New Content")
                .isLocked(true)
                .build();
        when(pageRepository.findById(1L)).thenReturn(Optional.of(rootPage));

        PageDetailDto result = pageService.updatePage(1L, updateDto);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Content", result.getContent());
        assertTrue(result.getIsLocked());
    }

    @Test
    void deletePage_WhenNotLocked_ShouldDelete() {
        when(pageRepository.findById(1L)).thenReturn(Optional.of(rootPage));

        pageService.deletePage(1L);

        verify(pageRepository).delete(rootPage);
    }

    @Test
    void deletePage_WhenLocked_ShouldThrowException() {
        rootPage.setIsLocked(true);
        when(pageRepository.findById(1L)).thenReturn(Optional.of(rootPage));

        assertThrows(IllegalStateException.class, () -> pageService.deletePage(1L));
        verify(pageRepository, never()).delete(any());
    }

    @Test
    void deletePage_WhenChildLocked_ShouldThrowException() {
        childPage.setIsLocked(true);
        when(pageRepository.findById(1L)).thenReturn(Optional.of(rootPage));

        assertThrows(IllegalStateException.class, () -> pageService.deletePage(1L));
        verify(pageRepository, never()).delete(any());
    }
}
