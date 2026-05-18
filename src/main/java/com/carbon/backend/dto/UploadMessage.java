package com.carbon.backend.dto;


import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UploadMessage(


        String uploadId,
        @NotBlank(message = "Filename cannot be null")
        String originalFilename,

        String storageKey,

        LocalDateTime uploadedAt

) {
}
