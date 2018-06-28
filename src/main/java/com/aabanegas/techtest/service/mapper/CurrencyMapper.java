package com.aabanegas.techtest.service.mapper;

import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Locale;

/**
 * Mapper for Currency and its DTO called CurrencyDTO. Normal mappers are generated using MapStruct.
 */
@Component
public class CurrencyMapper {

    Locale currentLocale = Locale.getDefault();

    public String asString(Currency currency) {
        return currency != null ?
                currency.getCurrencyCode() :
                Currency.getInstance(currentLocale).getCurrencyCode();
    }

    public Currency asCurrency(String currency) throws IllegalArgumentException {
        return currency == null || currency.isEmpty() ?
                Currency.getInstance(currentLocale):
                Currency.getInstance(currency);
    }
}
