package com.aabanegas.techtest.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

@Table(keyspace = "payments", name = "payment",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID uuid;
    
    @Size(max = 5, message= "Client references are composed of 5 digits")
    @Positive
    Integer clientRef;
	
	@Size(min = 16, max = 16, message= "Payment card numbers are composed of 16 digits")
	long cardNumber;
	
	@Positive
	BigDecimal amount;
	
	@PositiveOrZero
	float taxAmount;
	
	String currencyAlphaCode;

	LocalDate executionTime;
	
}
