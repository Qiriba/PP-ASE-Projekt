package com.banking.domain.model.AttributeConverter;

import com.banking.domain.model.valueobjects.AccountNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountNumberConverter implements AttributeConverter<AccountNumber, String> {

    @Override
    public String convertToDatabaseColumn(AccountNumber attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public AccountNumber convertToEntityAttribute(String dbData) {
        return dbData != null ? new AccountNumber(dbData) : null;
    }
}
