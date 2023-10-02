package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Audit{
	@JsonProperty("event_name")
	private String eventName;
	@JsonProperty("source_system")
	private String sourceSystem;
}

