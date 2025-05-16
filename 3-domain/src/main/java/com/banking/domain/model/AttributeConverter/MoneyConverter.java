package com.banking.infrastructure.persistence.converter;

import com.banking.domain.model.valueobjects.Money;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;
import java.util.Currency;

@Converter(autoApply = false)
public class MoneyConverter implements AttributeConverter<Money, String> {

    // Format: "100.00:EUR"
    @Override
    public String convertToDatabaseColumn(Money money) {
        if (money == null) return null;
        return money.getAmount().toPlainString() + ":" + money.getCurrency().getCurrencyCode();
    }

    @Override
    public Money convertToEntityAttribute(String dbData) {
        if (dbData == null || !dbData.contains(":")) return null;
        String[] parts = dbData.split(":");
        return new Money(new BigDecimal(parts[0]), Currency.getInstance(parts[1]));
    }
}
