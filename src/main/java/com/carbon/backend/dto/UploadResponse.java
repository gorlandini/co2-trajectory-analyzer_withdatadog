package com.carbon.backend.dto;

import java.time.LocalDateTime;

public record UploadResponse(

        String uploadId,
        String filename,
        String message,
        LocalDateTime timestamp

) {
}
