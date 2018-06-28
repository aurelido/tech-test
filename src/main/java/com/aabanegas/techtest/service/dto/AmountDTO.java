package com.aabanegas.techtest.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AmountDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Positive(message = "Positive value is required")
	BigDecimal value;
	
	@PositiveOrZero(message = "Positive or zero value is required")
	float tax;

	String currencyCode;

	public AmountDTO(BigDecimal value, float tax) {
		this.value = value;
		this.tax = tax;
	}
	
	public AmountDTO(BigDecimal value, float tax, String currencyCode) {
		this.value = value;
		this.tax = tax;
		this.currencyCode = currencyCode;
	}

}
