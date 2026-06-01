package com.kes.api.controller;

import com.kes.api.dto.AuditLogResponseDTO;
import com.kes.api.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogResponseDTO>> getAllLogs() {
        List<AuditLogResponseDTO> logs = auditLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}
