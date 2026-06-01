package com.kes.api.service;

import com.kes.api.dto.PageCreateDto;
import com.kes.api.dto.PageDetailDto;
import com.kes.api.dto.PageSummaryDto;
import com.kes.api.dto.PageUpdateDto;
import com.kes.api.entity.Page;
import com.kes.api.exception.ResourceNotFoundException;
import com.kes.api.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PageService {

    private final PageRepository pageRepository;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public List<PageSummaryDto> getRootPages() {
        return pageRepository.findByParentIsNull().stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageDetailDto getPageDetail(Long id) {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Page not found with id: " + id));
        return mapToDetailDto(page);
    }

    public PageDetailDto createPage(PageCreateDto createDto) {
        Page page = Page.builder()
                .title(createDto.getTitle())
                .isLocked(false)
                .build();

        if (createDto.getParentId() != null) {
            Page parent = pageRepository.findById(createDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent page not found with id: " + createDto.getParentId()));
            parent.addChild(page);
        }

        Page savedPage = pageRepository.save(page);
        auditLogService.log("CREATE", "PAGE", savedPage.getId().toString(), "Title: " + savedPage.getTitle());
        return mapToDetailDto(savedPage);
    }

    public PageDetailDto updatePage(Long id, PageUpdateDto updateDto) {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Page not found with id: " + id));

        StringBuilder details = new StringBuilder();
        if (updateDto.getTitle() != null) {
            details.append("Title changed from '").append(page.getTitle()).append("' to '").append(updateDto.getTitle()).append("'. ");
            page.setTitle(updateDto.getTitle());
        }
        if (updateDto.getContent() != null) {
            details.append("Content updated. ");
            page.setContent(updateDto.getContent());
        }
        if (updateDto.getIsLocked() != null) {
            details.append("Lock status changed from ").append(page.getIsLocked()).append(" to ").append(updateDto.getIsLocked()).append(". ");
            page.setIsLocked(updateDto.getIsLocked());
        }

        auditLogService.log("UPDATE", "PAGE", id.toString(), details.toString());
        return mapToDetailDto(page);
    }

    public void deletePage(Long id) {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Page not found with id: " + id));
        
        // StateGuard: Check if locked before deletion (including descendants)
        checkLockRecursive(page);

        String title = page.getTitle();
        pageRepository.delete(page);
        auditLogService.log("DELETE", "PAGE", id.toString(), "Title: " + title);
    }

    @Transactional(readOnly = true)
    public List<PageSummaryDto> searchPages(String query) {
        return pageRepository.searchPages(query).stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    private void checkLockRecursive(Page page) {
        if (Boolean.TRUE.equals(page.getIsLocked())) {
            throw new IllegalStateException("Cannot delete a locked page: " + page.getTitle());
        }
        for (Page child : page.getChildren()) {
            checkLockRecursive(child);
        }
    }

    private PageSummaryDto mapToSummaryDto(Page page) {
        return PageSummaryDto.builder()
                .id(page.getId())
                .title(page.getTitle())
                .hasChildren(!page.getChildren().isEmpty())
                .parentId(page.getParent() != null ? page.getParent().getId() : null)
                .build();
    }

    private PageDetailDto mapToDetailDto(Page page) {
        return PageDetailDto.builder()
                .id(page.getId())
                .title(page.getTitle())
                .content(page.getContent())
                .isLocked(page.getIsLocked())
                .parentId(page.getParent() != null ? page.getParent().getId() : null)
                .children(page.getChildren().stream()
                        .map(this::mapToSummaryDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
