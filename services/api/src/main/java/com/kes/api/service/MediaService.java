package com.kes.api.service;

import com.kes.api.dto.MediaUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class MediaService {

    @Value("${app.media.storage-path:/app/media/}")
    private String storagePath;

    @Value("${app.media.base-url:/media/}")
    private String baseUrl;

    public MediaUploadResponse uploadMedia(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;
            
            Path targetPath = Paths.get(storagePath).resolve(filename);
            Files.createDirectories(targetPath.getParent());
            Files.copy(file.getInputStream(), targetPath);

            log.info("Uploaded file {} to {}", originalFilename, targetPath);

            return new MediaUploadResponse(baseUrl + filename, filename);
        } catch (IOException e) {
            log.error("Failed to upload media", e);
            throw new RuntimeException("Failed to upload media", e);
        }
    }
}
