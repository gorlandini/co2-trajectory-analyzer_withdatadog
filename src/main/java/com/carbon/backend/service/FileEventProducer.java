package com.carbon.backend.service;

import com.carbon.backend.message.FileUploadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileEventProducer {

    private final KafkaTemplate<String, FileUploadEvent> kafkaTemplate;

    private static final String TOPIC = "file-uploaded";

    public void publish(FileUploadEvent event) {

        kafkaTemplate.send(TOPIC, event.uploadId().toString(), event)
                .whenComplete((result, ex) -> {

                    if (ex != null) {
                        System.err.println("❌ Erro ao enviar Kafka: " + ex.getMessage());
                        ex.printStackTrace();
                        return;
                    }

                    System.out.println("✅ Mensagem enviada com sucesso!");
                    System.out.println("Topic: " + result.getRecordMetadata().topic());
                    System.out.println("Partition: " + result.getRecordMetadata().partition());
                    System.out.println("Offset: " + result.getRecordMetadata().offset());
                });
    }
}