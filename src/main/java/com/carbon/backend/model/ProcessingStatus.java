package com.carbon.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "processing_status")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProcessingStatus {
    @Id
    private String uploadId; // UUID gerado no controller
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    private String filename;
    private Integer totalLines;
    private Integer validLines;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    public enum Status {
        QUEUED, // mensagem publicada no Kafka
        PROCESSING, // consumer pegou a mensagem
        DONE, // pipeline concluído
        FAILED // erro após todos os retries
    }
}