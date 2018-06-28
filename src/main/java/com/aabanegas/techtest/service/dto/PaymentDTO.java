package com.aabanegas.techtest.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO implements Serializable {
	private static final long serialVersionUID = 1L;
    
    String uuid;

    AmountDTO amount;

    LocalDate timeExec;

    String status;
	
}
