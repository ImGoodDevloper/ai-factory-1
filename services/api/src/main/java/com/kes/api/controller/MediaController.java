package com.kes.api.controller;

import com.kes.api.dto.MediaUploadResponse;
import com.kes.api.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    public MediaUploadResponse upload(@RequestParam("file") MultipartFile file) {
        return mediaService.uploadMedia(file);
    }
}
