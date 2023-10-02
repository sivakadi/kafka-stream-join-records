package com.example.dto.converters;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.example.dto.SalesRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SalesRecordSerializer implements Serializer<SalesRecord> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, SalesRecord data) {
        try {
        	if(data instanceof SalesRecord) {
            return objectMapper.writeValueAsBytes(data);
        	}else {
        		throw new ClassCastException("No more of this!");
        	}
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }

    }
}
