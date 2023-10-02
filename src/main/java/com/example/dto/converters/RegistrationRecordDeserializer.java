package com.example.dto.converters;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.example.dto.RegistrationRecord;
import com.fasterxml.jackson.databind.ObjectMapper;


public class RegistrationRecordDeserializer implements Deserializer<RegistrationRecord> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public RegistrationRecord deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, RegistrationRecord.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
