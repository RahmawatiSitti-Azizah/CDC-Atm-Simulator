package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.util.ErrorConstant;
import com.mitrais.cdc.util.StringMatcherUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;

public class UserInputServiceImplTest {
    private MockedStatic<StringMatcherUtil> mockStringMatcherUtil;
    private UserInputServiceImpl serviceInTest;

    @BeforeEach
    public void setUp() throws Exception {
        mockStringMatcherUtil = mockStatic(StringMatcherUtil.class);
        serviceInTest = new UserInputServiceImpl();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockStringMatcherUtil.close();
    }

    @Test
    public void testToValidatedMoney_whenInputValidLongValue_thenReturnMoneyCorrectly() throws Exception {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberOnly(anyString())).thenReturn(true);
        Money money = serviceInTest.toValidatedMoney("10");
        Assertions.assertTrue(money.toString().equals("$10"));
    }

    @Test
    public void testToValidatedMoney_whenInputInvalidLongValue_thenThrowException() {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberOnly(anyString())).thenReturn(false);
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.toValidatedMoney("123123s"));
        Assertions.assertEquals(ErrorConstant.INVALID_AMOUNT, exception.getMessage());
    }

    @Test
    public void testToValidatedMenu_whenInputValidLongValue_thenReturnIntValueCorrectly() throws Exception {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberWithLength(anyString(), anyInt())).thenReturn(true);
        Assertions.assertEquals(10, serviceInTest.toValidatedMenu("10"));
    }

    @Test
    public void testToValidatedMenu_whenInputInvalid_thenReturnZero() {
        mockStringMatcherUtil.when(() -> StringMatcherUtil.checkStringIsNumberWithLength(anyString(), anyInt())).thenReturn(false);
        Assertions.assertEquals(0, serviceInTest.toValidatedMenu("123123s"));
    }
}