package com.example.dto.converters;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.example.dto.SalesRecord;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SalesRecordDeserializer implements Deserializer<SalesRecord> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SalesRecord deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, SalesRecord.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
