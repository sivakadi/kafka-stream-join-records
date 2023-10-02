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
public class SalesRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonProperty("catalog_number")
	private String catalogNumber;
	@JsonProperty("order_number")
	private String orderNumber;
	@JsonProperty("quantity")
	private String quantity; 
	@JsonProperty("sales_date")
	private String salesDate; 
	@JsonProperty("country")
	private String country; 
	@JsonProperty("audit")
	private Audit audit;

}

