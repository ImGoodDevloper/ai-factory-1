package com.kes.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDetailDto {
    private Long id;
    private String title;
    private String content;
    private Boolean isLocked;
    private Long parentId;
    private List<PageSummaryDto> children;
}
