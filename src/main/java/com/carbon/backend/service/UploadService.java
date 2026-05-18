package com.carbon.backend.service;

import com.carbon.backend.dto.UploadMessage;
import com.carbon.backend.dto.UploadResponse;
import com.carbon.backend.message.FileUploadEvent;
import com.carbon.backend.model.ProcessingStatus;
import com.carbon.backend.repository.ProcessingStatusRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
    public class UploadService {

    private final S3Client s3Client;
    private final String bucket = "co2upload";
    private final FileEventProducer fileEventProducer;
    private final ProcessingStatusRepository processingStatusRepository;
    private final MeterRegistry meterRegistry;

        public UploadResponse process(MultipartFile file) throws IOException {

            log.info("Arquivo recebido: {}", file.getOriginalFilename());
            log.info("Tamanho: {}", file.getSize());
            log.info("Content-Type: {}", file.getContentType());



            String id = UUID.randomUUID().toString();
            String key = id + "-" + file.getOriginalFilename();

            // 1. Upload para S3
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            String fileUrl = "s3://" + bucket + "/" + key;


            ProcessingStatus status = ProcessingStatus.builder()
                    .uploadId(id)
                    .status(ProcessingStatus.Status.QUEUED)
                    .filename(file.getOriginalFilename())
                    .updatedAt(LocalDateTime.now())
                    .build();

            processingStatusRepository.save(status);



            // 2. cria evento
            FileUploadEvent event = new FileUploadEvent(
                    id,
                    "co2upload",
                    key,
                    file.getOriginalFilename(),
                    LocalDateTime.now()
            );

// 3. publica no Kafka
            fileEventProducer.publish(event);

            meterRegistry.counter("co2.upload.received").increment();

            return(new UploadResponse(
                         id,
                    file.getOriginalFilename(),
                    "Arquivo recebido com sucesso",
                         LocalDateTime.now()));



        }
   }



