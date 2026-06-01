package com.kes.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponseDTO {
    private Long id;
    private String username;
    private String action;
    private String resourceType;
    private String resourceId;
    private String details;
    private LocalDateTime timestamp;
}
