package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.util.ErrorConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.security.auth.login.CredentialNotFoundException;

class LoginServiceImplTest {

    private static final Dollar ACCOUNT_BALANCE = new Dollar(250.0);
    private static final String PIN = "123123";
    private static final String ACCOUNT_NUMBER = "112233";
    private AccountValidatorService validatorService = Mockito.mock(AccountValidatorService.class);
    private SearchAccountService searchAccountService = Mockito.mock(SearchAccountService.class);

    private LoginServiceImpl serviceInTest;

    @BeforeEach
    void setUp() {
        serviceInTest = new LoginServiceImpl(searchAccountService, validatorService);
    }

    @Test
    public void testLogin_whenAllParameterValid_theReturnAccount() throws Exception {
        AccountDto account = new AccountDto("Jane Doe", ACCOUNT_NUMBER, ACCOUNT_BALANCE);
        Mockito.when(searchAccountService.get(ACCOUNT_NUMBER, PIN)).thenReturn(account);

        AccountDto result = serviceInTest.login(ACCOUNT_NUMBER, PIN);

        Assertions.assertEquals(account.getAccountNumber(), result.getAccountNumber());
        Assertions.assertEquals(account.getAccountHolderName(), result.getAccountHolderName());
        Assertions.assertEquals(account.getBalance().toString(), result.getBalance().toString());
        Mockito.verify(validatorService, Mockito.times(1)).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(validatorService, Mockito.times(1)).validatePin(Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testLogin_whenAccountAndPinNull_thenThrowCredentialNotFoundExceptionException() throws Exception {
        Exception exception = Assertions.assertThrows(CredentialNotFoundException.class, () -> serviceInTest.login(null, null));

        Assertions.assertEquals(ErrorConstant.INVALID_ACCOUNT_PASSWORD, exception.getMessage());
        Mockito.verify(validatorService, Mockito.never()).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(validatorService, Mockito.never()).validatePin(Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.never()).get(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testLogin_whenAccountInvalid_thenThrowException() throws Exception {
        Mockito.doThrow(new Exception(ErrorConstant.ACCOUNT_NUMBER_SHOULD_HAVE_6_DIGITS_LENGTH)).when(validatorService).validateAccountNumber(Mockito.anyString(), Mockito.anyString());

        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.login(ACCOUNT_NUMBER, PIN));

        Assertions.assertEquals(ErrorConstant.ACCOUNT_NUMBER_SHOULD_HAVE_6_DIGITS_LENGTH, exception.getMessage());
        Mockito.verify(validatorService, Mockito.times(1)).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(validatorService, Mockito.never()).validatePin(Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.never()).get(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testLogin_whenPinInvalid_thenThrowException() throws Exception {
        Mockito.doThrow(new Exception(ErrorConstant.PIN_SHOULD_ONLY_CONTAINS_NUMBERS)).when(validatorService).validatePin(Mockito.anyString());

        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.login(ACCOUNT_NUMBER, PIN));

        Assertions.assertEquals(ErrorConstant.PIN_SHOULD_ONLY_CONTAINS_NUMBERS, exception.getMessage());
        Mockito.verify(validatorService, Mockito.times(1)).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(validatorService, Mockito.times(1)).validatePin(Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.never()).get(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testLogin_whenAccountNotFound_thenThrowException() throws Exception {
        Mockito.when(searchAccountService.get(ACCOUNT_NUMBER, PIN)).thenThrow(new Exception(ErrorConstant.INVALID_ACCOUNT_PASSWORD));

        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.login(ACCOUNT_NUMBER, PIN));

        Assertions.assertEquals(ErrorConstant.INVALID_ACCOUNT_PASSWORD, exception.getMessage());
        Mockito.verify(validatorService, Mockito.times(1)).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(validatorService, Mockito.times(1)).validatePin(Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testIsAuthenticated_ifSessionAccountFound_thenReturnTrue() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Account account = new Account(ACCOUNT_NUMBER, "Jane Doe", PIN, ACCOUNT_BALANCE);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        Mockito.when(mockSession.getAttribute("account")).thenReturn(account);

        Assertions.assertTrue(serviceInTest.isAuthenticated(mockRequest));
    }

    @Test
    public void testIsAuthenticated_ifSessionAccountNotFound_thenReturnFalse() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        Mockito.when(mockSession.getAttribute("account")).thenReturn(null);

        Assertions.assertFalse(serviceInTest.isAuthenticated(mockRequest));
    }
}