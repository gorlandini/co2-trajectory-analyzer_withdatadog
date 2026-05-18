package com.carbon.backend.controller;


import com.carbon.backend.dto.UploadResponse;
import com.carbon.backend.service.UploadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/uploads")
public class UploadController {

    private final UploadService uploadService;


    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping()
    public ResponseEntity<UploadResponse> upload(@Valid @RequestParam("file") MultipartFile file) {
        UploadResponse response = null;
        try {

            response =  uploadService.process(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        return ResponseEntity.ok(response);
    }

}
