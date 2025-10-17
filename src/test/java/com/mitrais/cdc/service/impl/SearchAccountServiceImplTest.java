package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import com.mitrais.cdc.repository.AccountRepository;
import com.mitrais.cdc.util.ErrorConstant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SearchAccountServiceImplTest {
    private AccountRepository repository = mock();
    private MockedStatic<RepositoryFactory> mockRepositoryFactory;
    private SearchAccountServiceImpl serviceInTest;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() throws Exception {
        serviceInTest = new SearchAccountServiceImpl(repository);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() throws Exception {
        System.setOut(standardOut);
    }

    public Account getAccount(Double balance, String name, String accountNo, String pin) {
        return new Account(new Dollar(balance), name, accountNo, pin);
    }

    @Test
    public void testAddAccount_whenNotDuplicate_thenSaveAccount() throws Exception {
        when(repository.findAccountByAccountNumber(anyString())).thenReturn(null);
        serviceInTest.addAccount(new Dollar(10.0), "Jane Doe", "112233", "123456");
        verify(repository, times(1)).findAccountByAccountNumber(anyString());
        verify(repository, times(1)).save(any());
    }

    @Test
    public void testAddAccount_whenDuplicateAccountNumberOnly_thenOutputDuplicateAccountNumber() {
        when(repository.findAccountByAccountNumber(anyString())).thenReturn(getAccount(100.0, "Jane Doe", "112233", "12345"));
        serviceInTest.addAccount(new Dollar(10.0), "Jane Doe", "112233", "123456");
        verify(repository, times(1)).findAccountByAccountNumber(anyString());
        verify(repository, never()).save(any());
        Assertions.assertTrue(outputStreamCaptor.toString().contains(ErrorConstant.DUPLICATE_ACCOUNT_NUMBER));
    }

    @Test
    public void testAddAccount_whenDuplicateAllRecordDetailsFound_thenOutputDuplicateRecordMessage() {
        when(repository.findAccountByAccountNumber(anyString())).thenReturn(getAccount(10.0, "Jane Doe", "112233", "12345"));
        serviceInTest.addAccount(new Dollar(10.0), "Jane Doe", "112233", "123456");
        verify(repository, times(1)).findAccountByAccountNumber(anyString());
        verify(repository, never()).save(any());
        Assertions.assertTrue(outputStreamCaptor.toString().contains(ErrorConstant.DUPLICATE_ACCOUNT_NUMBER));
    }

    @Test
    public void testGetAccount_whenValidAndAccountNumberFound_thenReturnAccount() throws Exception {
        Account account = getAccount(100.0, "Jane Doe", "112233", "123456");
        when(repository.findAccountByAccountNumber(anyString())).thenReturn(account);
        Account result = serviceInTest.get("112233", "123456");
        Assertions.assertEquals(account, result);
        verify(repository, times(1)).findAccountByAccountNumber(anyString());
    }

    @Test
    public void testGetAccount_whenInvalidPin_thenReturnInvalidAccountNumberPinException() {
        Account account = getAccount(100.0, "Jane Doe", "112233", "123456");
        when(repository.findAccountByAccountNumber(anyString())).thenReturn(account);
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.get("112233", "111111"));
        verify(repository, times(1)).findAccountByAccountNumber(anyString());
        Assertions.assertEquals("Invalid Account Number/PIN", exception.getMessage());
    }

    @Test
    public void testGet_whenValidAndFound_thenReturnAccount() throws Exception {
        Account account = getAccount(100.0, "Jane Doe", "112233", "123456");
        when(repository.findAccountByAccountNumber(anyString())).thenReturn(account);
        Account result = serviceInTest.get("112233");
        Assertions.assertEquals(account, result);
        verify(repository, times(1)).findAccountByAccountNumber(anyString());
    }

    @Test
    public void testGet_whenNotFound_thenReturnInvalidAccountException() throws Exception {
        when(repository.findAccountByAccountNumber(anyString())).thenReturn(null);
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.get("112233"));
        verify(repository, times(1)).findAccountByAccountNumber(anyString());
        Assertions.assertEquals("Invalid Account", exception.getMessage());
    }
}