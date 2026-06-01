package com.kes.api.controller;

import com.kes.api.dto.PageCreateDto;
import com.kes.api.dto.PageDetailDto;
import com.kes.api.dto.PageSummaryDto;
import com.kes.api.dto.PageUpdateDto;
import com.kes.api.service.PageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("/pages")
    @PreAuthorize("hasAnyRole('VIEWER', 'EDITOR', 'ADMIN')")
    public List<PageSummaryDto> getRootPages() {
        return pageService.getRootPages();
    }

    @GetMapping("/pages/{id}")
    @PreAuthorize("hasAnyRole('VIEWER', 'EDITOR', 'ADMIN')")
    public PageDetailDto getPageDetail(@PathVariable Long id) {
        return pageService.getPageDetail(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('VIEWER', 'EDITOR', 'ADMIN')")
    public List<PageSummaryDto> search(@RequestParam("q") String query) {
        return pageService.searchPages(query);
    }

    @PostMapping("/pages")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    public PageDetailDto createPage(@Valid @RequestBody PageCreateDto createDto) {
        return pageService.createPage(createDto);
    }

    @PutMapping("/pages/{id}")
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    public PageDetailDto updatePage(@PathVariable Long id, @Valid @RequestBody PageUpdateDto updateDto) {
        return pageService.updatePage(id, updateDto);
    }

    @DeleteMapping("/pages/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    public void deletePage(@PathVariable Long id) {
        pageService.deletePage(id);
    }
}
