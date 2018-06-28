package com.aabanegas.techtest.service.mapper;

import com.aabanegas.techtest.domain.Payment;
import com.aabanegas.techtest.service.dto.AmountDTO;
import com.aabanegas.techtest.service.dto.PaymentDTO;
import com.aabanegas.techtest.service.dto.TransactionDTO;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Locale;

/**
 * Utilities to convert DTOs to Entities and reversed
 */
@Component
public class MapperUtils {

    final private Locale currentLocale;

    public MapperUtils(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

    /**
     * Converts the given Transaction DTO to a Payment entity
     *
     * @param transactionDto Given DTO
     * @return Payment entity
     */
    public Payment transactionDtoToPayment(TransactionDTO transactionDto) {
        if (transactionDto == null) {
            return null;
        }
        AmountDTO amount = transactionDto.getAmount();
        // if null set local Currency
        Currency currency = asCurrency(amount.getCurrencyCode());

        return Payment.builder()
                    .clientRef(transactionDto.getClientRef())
                    .cardNumber(transactionDto.getCreditCard().getCardNumber())
                    .taxAmount(amount.getTax())
                    .amount(amount.getValue())
                    .currencyAlphaCode(currency.getCurrencyCode()).build();
    }

    /**
     * Converts the given Payment entity to a Payment DTO
     *
     * @param paymnet Given Entity
     * @return Payment DTO
     */
    public PaymentDTO paymentToPaymentDto(Payment paymnet) {
        if (paymnet == null) {
            return null;
        }

        AmountDTO amount = new AmountDTO(paymnet.getAmount(), paymnet.getTaxAmount(), paymnet.getCurrencyAlphaCode());
        return PaymentDTO.builder()
                    .uuid(paymnet.getUuid().toString())
                    .amount(amount)
                    .timeExec(paymnet.getExecutionTime()).build();
    }


    private String asString(Currency currency) {
        return currency != null ?
                currency.getCurrencyCode() :
                Currency.getInstance(currentLocale).getCurrencyCode();
    }

    /**
     * Paser the string to a valid Currency object.
     * @param currency ISO 4217 currency code
     * @return Resolved currency. Local currency instance if null
     * @throws IllegalArgumentException
     */
    private Currency asCurrency(String currency) throws IllegalArgumentException {
        return currency == null || currency.isEmpty() ?
                Currency.getInstance(currentLocale) :
                Currency.getInstance(currency);
    }

    //List<TransactionDTO> paymentsToPaymentDtos(List<Payment> payments);
}