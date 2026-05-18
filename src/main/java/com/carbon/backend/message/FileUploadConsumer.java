package com.carbon.backend.message;




import com.carbon.backend.model.ProcessingStatus;
import com.carbon.backend.repository.ProcessingStatusRepository;
import com.carbon.backend.service.PipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadConsumer {

    private final ProcessingStatusRepository repository;
    private final PipelineService pipelineService;

    @KafkaListener(topics = "file-uploaded", groupId = "file-processor-group")
    public void consume(FileUploadEvent event) {

        log.info("📥 Evento recebido: {}", event.uploadId());

        // 1. busca status existente
        ProcessingStatus status = repository.findById(event.uploadId())
                .orElseThrow(() -> new RuntimeException("Upload não encontrado: " + event.uploadId()));

        // 2. marca como PROCESSING
        status.setStatus(ProcessingStatus.Status.PROCESSING);
        status.setUpdatedAt(LocalDateTime.now());
        repository.save(status);

        try {

            pipelineService.run(event);





            // 3. atualiza como DONE
            status.setStatus(ProcessingStatus.Status.DONE);
            status.setTotalLines(100);   // exemplo
            status.setValidLines(95);    // exemplo
            status.setUpdatedAt(LocalDateTime.now());

            repository.save(status);

            log.info("✅ Processamento concluído: {}", event.uploadId());

        } catch (Exception e) {

            status.setStatus(ProcessingStatus.Status.FAILED);
            status.setUpdatedAt(LocalDateTime.now());
            repository.save(status);

            log.error("❌ Falha no processamento: {}", event.uploadId(), e);
        }
    }
}