package com.kes.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageSummaryDto {
    private Long id;
    private String title;
    private Boolean hasChildren;
    private Long parentId;
}
