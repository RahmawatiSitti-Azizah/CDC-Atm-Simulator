package com.mitrais.cdc.model.converter;

import com.mitrais.cdc.model.Account;
import jakarta.persistence.AttributeConverter;

public class AccountConverter implements AttributeConverter<Account, String> {
    @Override
    public String convertToDatabaseColumn(Account attribute) {
        if (attribute != null) {
            return attribute.getAccountNumber();
        }
        return null;
    }

    @Override
    public Account convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            return new Account(null, null, dbData, null);
        }
        return null;
    }
}
