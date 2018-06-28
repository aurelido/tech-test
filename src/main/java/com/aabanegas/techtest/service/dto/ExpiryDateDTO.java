package com.aabanegas.techtest.service.dto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.joda.time.YearMonth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ExpiryDateDTO {

	@JsonFormat(shape = Shape.STRING, pattern = "YYYY-MM")
	@NotNull
	@FutureOrPresent
	private YearMonth expiryDate;

	public ExpiryDateDTO(@NotEmpty(message = "Expiry year is a required field") int expiryYear,
                         @NotEmpty(message = "Expiry month is a required field") int expiryMonth) {
		super();
		expiryDate = new YearMonth(expiryYear, expiryMonth);
	}

	/**
	 * Generate the JSON representation
	 */
	@JsonValue
	public String toString() {
		return String.join(",", "expiryMonth", String.valueOf(expiryDate.getMonthOfYear()), 
				"expiryYear", String.valueOf(expiryDate.getYear()));
	}
}
