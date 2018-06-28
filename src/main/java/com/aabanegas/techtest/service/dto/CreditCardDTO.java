package com.aabanegas.techtest.service.dto;

import java.io.Serializable;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Size(min = 16, max = 16, message= "Payment card numbers are composed of 16 digits")
	long cardNumber;
	
	ExpiryDateDTO expiryDate;
}
