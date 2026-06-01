package com.kes.api.service;

import com.kes.api.dto.AuditLogResponseDTO;
import com.kes.api.entity.AuditLog;
import com.kes.api.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String action, String resourceType, String resourceId, String details) {
        String username = SecurityContextHolder.getContext().getAuthentication() != null ?
                SecurityContextHolder.getContext().getAuthentication().getName() : "SYSTEM";

        AuditLog log = AuditLog.builder()
                .username(username)
                .action(action)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> getAllLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuditLogResponseDTO convertToDTO(AuditLog log) {
        return AuditLogResponseDTO.builder()
                .id(log.getId())
                .username(log.getUsername())
                .action(log.getAction())
                .resourceType(log.getResourceType())
                .resourceId(log.getResourceId())
                .details(log.getDetails())
                .timestamp(log.getTimestamp())
                .build();
    }
}
