package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.util.ErrorConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionAmountValidatorServiceImplTest {
    private TransactionAmountValidatorServiceImpl serviceInTest;

    @BeforeEach
    public void setUp() throws Exception {
        serviceInTest = new TransactionAmountValidatorServiceImpl();
    }

    @Test
    public void testValidateWithdrawAmount_whenAmountValid_thenNoExceptionThrow() throws Exception {
        Money money = mock(Money.class);
        when(money.isMultipleOf(anyInt())).thenReturn(true);
        when(money.isMoreThan(any())).thenReturn(false);
        serviceInTest.validateWithdrawAmount(money);
        verify(money, times(1)).isMultipleOf(anyInt());
        verify(money, times(1)).isMoreThan(any());
    }

    @Test
    public void testValidateWithdrawAmount_whenAmountNotMultipleOfTen_thenThrowInvalidAmountException() throws Exception {
        Money money = mock(Money.class);
        when(money.isMultipleOf(anyInt())).thenReturn(false);
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.validateWithdrawAmount(money));
        Assertions.assertEquals(ErrorConstant.INVALID_AMOUNT, exception.getMessage());
        verify(money, times(1)).isMultipleOf(anyInt());
        verify(money, never()).isMoreThan(any());
    }

    @Test
    public void testValidateWithdrawAmount_whenAmountExceed1000_thenThrowMaximumExceedException() throws Exception {
        Money money = mock(Money.class);
        when(money.isMultipleOf(anyInt())).thenReturn(true);
        when(money.isMoreThan(any())).thenReturn(true);
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.validateWithdrawAmount(money));
        Assertions.assertEquals(ErrorConstant.MAXIMUM_TRANSACTION_AMOUNT_EXCEED, exception.getMessage());
        verify(money, times(1)).isMultipleOf(anyInt());
        verify(money, times(1)).isMoreThan(any());
    }
}