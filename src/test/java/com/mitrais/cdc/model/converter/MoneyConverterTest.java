package com.mitrais.cdc.model.converter;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoneyConverterTest {
    private MoneyConverter converterInTest = new MoneyConverter();

    @Test
    public void testConvertToDatabaseColumn_whenParameterValidNotNull_thenReturnAmountFromMoneyObject() {
        Double result = converterInTest.convertToDatabaseColumn(new Dollar(10.0));
        Assertions.assertEquals(10.0, result);
    }

    @Test
    public void testConvertToDatabaseColumn_whenParameterInvalid_thenReturnZero() {
        Double result = converterInTest.convertToDatabaseColumn(new Dollar(null));
        Assertions.assertEquals(0.0, result);
    }

    @Test
    public void testconvertToEntityAttribute_whenParameterValidNotNull() {
        Money result = converterInTest.convertToEntityAttribute(10.0);
        Assertions.assertEquals((new Dollar(10.0)).toString(), result.toString());
    }
}