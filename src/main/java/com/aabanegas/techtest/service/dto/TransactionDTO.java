package com.aabanegas.techtest.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

    @Size(max = 5, message= "Client references are composed of 5 digits")
    @Positive
    int clientRef;

	CreditCardDTO creditCard;

    AmountDTO amount;
	
}
