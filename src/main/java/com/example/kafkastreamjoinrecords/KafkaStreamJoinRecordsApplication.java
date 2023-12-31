package com.example.kafkastreamjoinrecords;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.kafka.streams.kstream.Predicate;
import java.util.function.Supplier;

import org.apache.kafka.clients.consumer.OffsetOutOfRangeException;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.UnlimitedWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;

import com.example.dto.Audit;
import com.example.dto.JoinedRecord;
import com.example.dto.OrderKey;
import com.example.dto.RegistrationRecord;
import com.example.dto.SalesRecord;
import com.example.dto.converters.CustomSerdes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class KafkaStreamJoinRecordsApplication {
	Logger logger = Logger.getLogger(KafkaStreamJoinRecordsApplication.class.getName());
	
	@Value("${valid.country.code:001}")
	private String validCountryCode;
	@Value("${valid.catalognumber.length:5}")
	private int validCatalogNumberLength;
	@Value("${valid.date.format:yyyy-MM-dd'T'HH:mm:ss.SSS}")
	private String validDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private final SimpleDateFormat validDateFormatter = new SimpleDateFormat(validDateFormat);
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final String[] countries = {"001", "002", "003"};
	private static final String[] catalog_numbers = {"10001", "20002", "30003", "1001"};
	private static final String[] model = {"1001-1", "2002-2", "3003-2"};
	private static final String[] order_number = {"1001-1", "2002-2", "3003-2"};
	private static final String[] dates = {"2017-03-17T02:43:48.111", "2023-05-31T10:18:54.472", "2018-12-09T11:18:21.989",
			"2023-06-30T18:21:31.000000", "2023-06-3018:21:31.000000Z", "2023-06-30T18:21:1.000000Z"};
	private int rRecordCount = 0;
	private int sRecordCount = 0;
	private int jRecordCount = 0;
	private int invalidRecordCount = 0;
	private int cRecordCount = 0;
	
	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamJoinRecordsApplication.class, args);
	}

	@Bean
	public Supplier<org.springframework.messaging.Message<RegistrationRecord>> registrationrecordproducer(){
		return () -> {
			int index = new Random().nextInt(countries.length);
			String country = countries[index];
			String catalogNumber = catalog_numbers[new Random().nextInt(catalog_numbers.length)];
			Audit audit = new Audit();
			audit.setEventName("Registration " + country + " " + catalogNumber);
			audit.setSourceSystem("RGR");
			RegistrationRecord regEvent = new RegistrationRecord(
					catalogNumber, true, model[index], "int7218",
					"int4123", "REG03814", dates[new Random().nextInt(dates.length)], country, audit);
			OrderKey oKey = new OrderKey(catalogNumber, country);

			logger.log(Level.INFO, "produced orderKey -> " + oKey);
			try {
				logger.log(Level.INFO,"produced rRecord -> " + objectMapper.writeValueAsString(regEvent));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.log(Level.INFO,"produced rRecordCount -> " + rRecordCount++);

			return MessageBuilder.withPayload(regEvent)
						.setHeader(KafkaHeaders.KEY, oKey)
						.build();
		};
	}

	@Bean
	public Supplier<org.springframework.messaging.Message<SalesRecord>> salesrecordproducer() {
		return () -> {
			int index = new Random().nextInt(countries.length);
			String country = countries[index];
			String catalogNumber = catalog_numbers[new Random().nextInt(catalog_numbers.length)];
			Audit audit = new Audit();
			audit.setEventName("Sales Event, " + country + " " + catalogNumber);
			audit.setSourceSystem("SLS");
			SalesRecord salEvent = new SalesRecord(
					catalogNumber, order_number[index], "2", dates[new Random().nextInt(dates.length)], country, audit);
			OrderKey oKey = new OrderKey(catalogNumber, country);

			logger.log(Level.INFO, "produced orderKey -> " + oKey);
			try {
				logger.log(Level.INFO, "produced sRecord -> " + objectMapper.writeValueAsString(salEvent));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.log(Level.INFO, "produced sRecordCount -> " + sRecordCount++);
			
			return MessageBuilder.withPayload(salEvent)
					.setHeader(KafkaHeaders.KEY, oKey)
					.build();
		};

	}
	
	@SuppressWarnings("unchecked")
	@Bean
	public BiFunction<KStream<OrderKey, RegistrationRecord>, KStream<OrderKey, SalesRecord>,
		KStream<OrderKey, JoinedRecord>> joinrecords() {
		//KStream<?, JoinedRecord>[]> joinrecords() {
		try {
			return (input1, input2) -> input1.join(input2, 
				(value1, value2) -> {
					JoinedRecord jRecord = null;
					jRecord = new JoinedRecord(
							value1.getCatalogNumber(), value1.getIsSelling(), value1.getModel(), value1.getProductId(), 
							value1.getRegistrationId(), value1.getRegistrationNumber(), value1.getSellingStatusDate(), 
							value1.getCountry(), value2.getCatalogNumber(), value2.getQuantity(), value2.getSalesDate(), 
							new Audit[] { value1.getAudit(), value2.getAudit()});
						return jRecord;
					},
					JoinWindows.ofTimeDifferenceAndGrace(Duration.of(10, ChronoUnit.SECONDS), Duration.of(10, ChronoUnit.MILLIS)),
					StreamJoined.with(CustomSerdes.OrderKey(), CustomSerdes.RegistrationRecord(), CustomSerdes.SalesRecord()
					))
					.filter((key, value) -> {
						if(value.getCountry().equals(validCountryCode) && isValidJoinedRecord(value)) {
							logger.log(Level.INFO, "filtered jRecordCount -> " + jRecordCount++);
							return true;
						}else {
							logger.log(Level.INFO, "invalid jRecordCount -> " + invalidRecordCount++);
							return false;
						}
					})
					.peek((key,value) -> logger.log(Level.INFO, "joined -> " + key + ": value: "+ value));
					//.split()
					//.branch((key, value) -> (isValidJoinedRecord(value)), (key, value) -> (!isValidJoinedRecord(value)));
		} catch (OffsetOutOfRangeException e) {
		    //TODO handle offset out of range exception by sending the messages to a DLQ
			logger.log(Level.SEVERE, "out of range error occurred while consuming messages from Kafka");
		    System.out.println("Offset out of range error occurred while consuming messages from Kafka");
		} catch (SerializationException e) {
		    //TODO handle deserialization exception by sending the messages to a DLQ
			logger.log(Level.SEVERE, "Serialization error occurred while consuming message from Kafka");
		} catch (TimeoutException e) {
		    //TODO handle timeout exception by sending the messages to a DLQ
			logger.log(Level.SEVERE, "Timeout occurred while consuming message from Kafka");
		} catch (KafkaException e) {
		    //TODO handle Kafka exception by sending the messages to a DLQ
			logger.log(Level.SEVERE, "Error occurred while consuming messages from Kafka:{} " , e.getMessage());
		} 
		return null;
	}
	
	//@Bean
	@SuppressWarnings("unchecked")
	public Function<KStream<Object, JoinedRecord>, KStream<?, JoinedRecord>[]> validateandbranchrecords() {

		Predicate<Object, JoinedRecord> validRecord = (k, v) -> isValidJoinedRecord(v);
		Predicate<Object, JoinedRecord> invalidRecord = (k, v) -> !isValidJoinedRecord(v);
		
		return input -> input
				.branch(validRecord, invalidRecord);
	}
		
	private boolean validateDateFormat(String date) {
		try {
			validDateFormatter.parse(date);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, e.getMessage());
			return false;
		}
		return true;
	}

	@Bean
	public Consumer<JoinedRecord> joinrecordconsumer() {
	    return jRecord -> {
	    	try {
				logger.log(Level.INFO, "consumed jRecord == " + objectMapper.writeValueAsString(jRecord));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	logger.log(Level.INFO, "consumed jRecordCount -> " + cRecordCount++);
		};
	}
	
	@Bean
	public Consumer<JoinedRecord> invalidrecordconsumer() {
	    return message -> {
	    	logger.log(Level.INFO, "consumed invalid jRecord == " + message);
	    	logger.log(Level.INFO, "consumed invalid jRecordCount -> " + cRecordCount++);
		};
	}

	private boolean isValidJoinedRecord(JoinedRecord jRecord) {
		return 
				jRecord.getCatalogNumber().length() == validCatalogNumberLength
				&& validateDateFormat(jRecord.getSellingStatusDate())
				&& validateDateFormat(jRecord.getSalesDate()); 

	}
}