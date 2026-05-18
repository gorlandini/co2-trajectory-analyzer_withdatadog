package com.carbon.backend.beam.transform;

import com.carbon.backend.model.Co2Record;
import org.apache.beam.sdk.transforms.DoFn;

public class ParseCsvFn extends DoFn<String, Co2Record> {

    @ProcessElement
    public void processElement(@Element String line, OutputReceiver<Co2Record> out) {
        String[] p = line.split(",");

        // ignora header
        if (p[0].equals("country")) return;

        Co2Record record = Co2Record.of(
                p[0],
                Integer.parseInt(p[1]),
                parseDouble(p[2]),
                parseDouble(p[3])
        );

        out.output(record);
    }

    private Double parseDouble(String v) {
        return (v == null || v.isEmpty()) ? 0.0 : Double.parseDouble(v);
    }
}