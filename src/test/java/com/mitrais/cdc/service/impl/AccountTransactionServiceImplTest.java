package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.AccountRepository;
import com.mitrais.cdc.repo.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AccountTransactionServiceImplTest {
    private AccountTransactionServiceImpl serviceInTest;
    private TransactionRepository transactionRepo = Mockito.mock(TransactionRepository.class);
    private AccountRepository accountRepo = Mockito.mock(AccountRepository.class);

    @BeforeEach
    public void setUp() throws Exception {
        serviceInTest = new AccountTransactionServiceImpl(transactionRepo, accountRepo);
    }

    @Test
    public void testWithdraw_transactionValid_thenBalanceDeducted() throws Exception {
        Account account = new Account(new Dollar(100), "Jane", "112233", "112211");
        serviceInTest.withdraw(account, new Dollar(20));
        Assertions.assertEquals(account.getStringBalance(), "$80");
        Mockito.verify(transactionRepo, Mockito.times(1)).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.times(1)).updateAccount(Mockito.any());
    }

    @Test
    public void testWithdraw_transactionAmountExceedAccountBalance_thenThrowException() throws Exception {
        Account account = new Account(new Dollar(100), "Jane", "112233", "112211");
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.withdraw(account, new Dollar(110)));
        Assertions.assertEquals("Insufficient balance $110", exception.getMessage());
        Assertions.assertEquals(account.getStringBalance(), "$100");
        Mockito.verify(transactionRepo, Mockito.never()).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.never()).updateAccount(Mockito.any());
    }

    @Test
    public void testTransfer_transactionValid_thenSuccessDeductedSourceBalanceAndIncreaseDestBalance() throws Exception {
        Account sourceAccount = new Account(new Dollar(100), "Jane", "112233", "112211");
        Account destinationAccount = new Account(new Dollar(50), "John", "223344", "334422");
        serviceInTest.transfer(sourceAccount, destinationAccount, new Dollar(20), "REF123");
        Assertions.assertEquals(sourceAccount.getStringBalance(), "$80");
        Assertions.assertEquals(destinationAccount.getStringBalance(), "$70");
        Mockito.verify(transactionRepo, Mockito.times(1)).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.times(2)).updateAccount(Mockito.any());
    }

    @Test
    public void testTransfer_transactionInvalid_thenThrowException() throws Exception {
        Account sourceAccount = new Account(new Dollar(100), "Jane", "112233", "112211");
        Account destinationAccount = new Account(new Dollar(50), "John", "223344", "334422");
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.transfer(sourceAccount, destinationAccount, new Dollar(110), "REF123"));
        Assertions.assertEquals("Insufficient balance $110", exception.getMessage());
        Assertions.assertEquals(sourceAccount.getStringBalance(), "$100");
        Assertions.assertEquals(destinationAccount.getStringBalance(), "$50");
        Mockito.verify(transactionRepo, Mockito.never()).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.never()).updateAccount(Mockito.any());
    }
}