package com.example.dto.converters;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import com.example.dto.JoinedRecord;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JoinedRecordDeserializer implements Deserializer<JoinedRecord> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JoinedRecord deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, JoinedRecord.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
