package com.example.dto.converters;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.dto.JoinedRecord;
import com.example.dto.OrderKey;
import com.example.dto.RegistrationRecord;
import com.example.dto.SalesRecord;

public class CustomSerdes {
	    private CustomSerdes() {}
	    public static Serde<com.example.dto.OrderKey> OrderKey() {
	JsonSerializer<com.example.dto.OrderKey> serializer = new JsonSerializer<>();
	JsonDeserializer<com.example.dto.OrderKey> deserializer = new JsonDeserializer<>(com.example.dto.OrderKey.class);
	return Serdes.serdeFrom(serializer, deserializer);
	    }

	    public static Serde<com.example.dto.RegistrationRecord> RegistrationRecord() {
	JsonSerializer<com.example.dto.RegistrationRecord> serializer = new JsonSerializer<>();
	JsonDeserializer<com.example.dto.RegistrationRecord> deserializer = new JsonDeserializer<>(com.example.dto.RegistrationRecord.class);
	return Serdes.serdeFrom(serializer, deserializer);
	    }

	    public static Serde<com.example.dto.SalesRecord> SalesRecord() {
	JsonSerializer<com.example.dto.SalesRecord> serializer = new JsonSerializer<>();
	JsonDeserializer<com.example.dto.SalesRecord> deserializer = new JsonDeserializer<>(com.example.dto.SalesRecord.class);
	return Serdes.serdeFrom(serializer, deserializer);
	    }

	    public static Serde<com.example.dto.JoinedRecord> JoinedRecord() {
	JsonSerializer<com.example.dto.JoinedRecord> serializer = new JsonSerializer<>();
	JsonDeserializer<com.example.dto.JoinedRecord> deserializer = new JsonDeserializer<>(com.example.dto.JoinedRecord.class);
	return Serdes.serdeFrom(serializer, deserializer);
	    }

	    
	}

