package com.example.dto.converters;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.example.dto.OrderKey;
import com.fasterxml.jackson.databind.ObjectMapper;


public class OrderKeyDeserializer implements Deserializer<OrderKey> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OrderKey deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, OrderKey.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
