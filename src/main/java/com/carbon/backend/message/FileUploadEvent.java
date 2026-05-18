package com.carbon.backend.message;


import java.time.LocalDateTime;
import java.util.UUID;

public record FileUploadEvent(
        String uploadId,
        String bucket,
        String key,
        String originalFilename,
        LocalDateTime uploadedAt
) {}