package com.carbon.backend.util;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class S3DownloadFile {

    private final S3Client s3Client;

    public S3DownloadFile(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public File downloadToTempFile(String bucket, String key) {
        try {
            System.out.println("Bucket:====> " + bucket);
            System.out.println("Key:====> " + key);

            Path tempFile = Files.createTempFile("beam-", ".csv");

            Files.deleteIfExists(tempFile);

            s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket( bucket )
                            .key(key)
                            .build(),
                    tempFile
            );

            return tempFile.toFile();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar arquivo do S3", e);
        }
    }
}