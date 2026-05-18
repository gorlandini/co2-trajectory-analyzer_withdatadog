package com.carbon.backend.beam.transform;

import com.carbon.backend.model.Co2Record;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BeamJobRunner {

    public void execute(String bucket, String key, String uploadId, File file) {
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline p = Pipeline.create(options);





        PCollection<String> lines =
                p.apply(TextIO.read().from(file.getAbsolutePath()));

        PCollection<Co2Record> records =
                lines.apply(ParDo.of(new ParseCsvFn()));



        PCollection<KV<String, Double>> byCountry =
                records.apply(MapElements.into(
                                TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.doubles()))
                        .via(r -> KV.of(
                                r.getCountry(),
                                r.getCo2PerCapita() == null ? 0.0 : r.getCo2PerCapita()
                        )));

        byCountry.apply("PrintToConsole",
                ParDo.of(new PrintFn())
        );




        p.run().waitUntilFinish();

    }


}