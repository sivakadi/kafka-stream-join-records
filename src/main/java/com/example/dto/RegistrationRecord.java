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
public class RegistrationRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonProperty("catalog_number")
	private String catalogNumber;
	@JsonProperty("is_selling")
	private Boolean isSelling;
	@JsonProperty("model")
	private String model;
	@JsonProperty("product_id")
	private String productId; 
	@JsonProperty("registration_id")
	private String registrationId;
	@JsonProperty("registration_number")
	private String registrationNumber;
	@JsonProperty("selling_status_date")
	private String sellingStatusDate;
	@JsonProperty("country")
	private String country; 
	@JsonProperty("audit")
	private Audit audit;

}
