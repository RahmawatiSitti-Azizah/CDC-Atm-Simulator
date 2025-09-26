package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.util.StringMatcherUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class AccountValidatorServiceImplTest {
    private AccountValidatorServiceImpl serviceInTest = new AccountValidatorServiceImpl();
    private MockedStatic<StringMatcherUtil> mockStringMatcherUtil;

    @BeforeEach
    public void setUp() throws Exception {
        mockStringMatcherUtil = Mockito.mockStatic(StringMatcherUtil.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockStringMatcherUtil.close();
    }

    @Test
    public void testValidateAccountNumber_validAccountNumber_thenSuccessWithoutException() throws Exception {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberOnly(Mockito.any())).thenReturn(true);
        serviceInTest.validateAccountNumber("123456", "Invalid account number");
    }

    @Test
    public void testValidateAccountNumber_invalidAccountNumberLength_thenThrowException() throws Exception {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberOnly(Mockito.any())).thenReturn(true);
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.validateAccountNumber("11221", "Invalid account number"));
        Assertions.assertEquals("Account Number should have 6 digits length", exception.getMessage());
    }

    @Test
    public void testValidateAccountNumber_nonNumericAccountNumber_thenThrowException() throws Exception {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberOnly(Mockito.any())).thenReturn(false);
        String errorMessage = "Invalid account number";
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.validateAccountNumber("1234a6", errorMessage));
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testValidatePin_validPin_thenSuccessWithoutException() throws Exception {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberWithLength(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        serviceInTest.validatePin("123456");
        mockStringMatcherUtil.verify(() -> StringMatcherUtil.checkStringIsNumberWithLength(Mockito.anyString(), Mockito.anyInt()), Mockito.times(1));
    }

    @Test
    public void testValidatePin_withAlfaNumericPin_thenThrowException() throws Exception {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberWithLength(Mockito.anyString(), Mockito.anyInt())).thenReturn(false);
        Assertions.assertThrows(Exception.class, () -> serviceInTest.validatePin("12skdf"));
    }

    @Test
    public void testValidatePin_withPinLengthExceed_thenThrowException() throws Exception {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberWithLength(Mockito.anyString(), Mockito.anyInt())).thenReturn(false);
        Assertions.assertThrows(Exception.class, () -> serviceInTest.validatePin("1234567"));
        mockStringMatcherUtil.verify(() -> StringMatcherUtil.checkStringIsNumberWithLength(Mockito.anyString(), Mockito.anyInt()), Mockito.never());
    }

    @Test
    public void testValidatePin_withPinNull_thenThrowException() throws Exception {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberWithLength(Mockito.anyString(), Mockito.anyInt())).thenReturn(false);
        Assertions.assertThrows(Exception.class, () -> serviceInTest.validatePin(null));
        mockStringMatcherUtil.verify(() -> StringMatcherUtil.checkStringIsNumberWithLength(Mockito.anyString(), Mockito.anyInt()), Mockito.never());
    }
}