package com.carbon.backend.service;

import com.carbon.backend.beam.transform.BeamJobRunner;
import com.carbon.backend.message.FileUploadEvent;
import com.carbon.backend.util.S3DownloadFile;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class PipelineService {

    private final BeamJobRunner beamJobRunner;
    private final S3DownloadFile s3DownloadFile;
    private final MeterRegistry meterRegistry;
    Timer.Sample sample;


    public void run(FileUploadEvent event) {
        File tempFile = s3DownloadFile.downloadToTempFile(event.bucket(), event.key());

        sample = Timer.start(meterRegistry);

        try {
            beamJobRunner.execute(event.bucket(), event.key(), event.uploadId(), tempFile);


        } finally {

            sample.stop(
                    Timer.builder("co2.pipeline.duration")
                            .description("Tempo do pipeline")
                            .register(meterRegistry)
            );
        }


    }
}
