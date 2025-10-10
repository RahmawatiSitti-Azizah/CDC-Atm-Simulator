package com.mitrais.cdc.model.converter;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Money;

import jakarta.persistence.AttributeConverter;

public class MoneyConverter implements AttributeConverter<Money, Double>{

	@Override
	public Double convertToDatabaseColumn(Money attribute) {
		try{
			Double value = Double.valueOf(attribute.toString().substring(1));
			return value;
		}catch (NumberFormatException e) {
			return 0.0;
		}
	}

	@Override
	public Money convertToEntityAttribute(Double dbData) {
		return new Dollar(dbData);
	}
}
