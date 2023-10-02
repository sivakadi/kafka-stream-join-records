package com.example.dto;

import java.io.Serializable;

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
public class OrderKey implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonProperty("catalog_number")
	String catalogNumber;
	@JsonProperty("country")
	String country;
}

