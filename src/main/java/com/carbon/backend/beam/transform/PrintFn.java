package com.carbon.backend.beam.transform;



import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

public class PrintFn extends DoFn<KV<String, Double>, Void> {

    @ProcessElement
    public void processElement(@Element KV<String, Double> element) {
        System.out.println(
                "Country: " + element.getKey()
                        + " | Value: " + element.getValue()
        );
    }
}