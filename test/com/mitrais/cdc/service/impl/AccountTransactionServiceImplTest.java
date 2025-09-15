package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.AccountRepository;
import com.mitrais.cdc.repo.TransactionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AccountTransactionServiceImplTest {
    private AccountTransactionServiceImpl serviceInTest;
    private TransactionRepository transactionRepo = Mockito.mock(TransactionRepository.class);
    private AccountRepository accountRepo = Mockito.mock(AccountRepository.class);

    @Before
    public void setUp() throws Exception {
        serviceInTest = new AccountTransactionServiceImpl(transactionRepo, accountRepo);
    }

    @Test
    public void testWithdraw_transactionValid_thenBalanceDeducted() throws Exception {
        Account account = new Account(new Dollar(100), "Jane", "112233", "112211");
        serviceInTest.withdraw(account, new Dollar(20));
        Assert.assertEquals(account.getStringBalance(), "$80");
        Mockito.verify(transactionRepo, Mockito.times(1)).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.times(1)).updateAccount(Mockito.any());
    }

    @Test
    public void testWithdraw_transactionAmountExceedAccountBalance_thenThrowException() throws Exception {
        Account account = new Account(new Dollar(100), "Jane", "112233", "112211");
        Exception exception = Assert.assertThrows(Exception.class, () -> serviceInTest.withdraw(account, new Dollar(110)));
        Assert.assertEquals("Insufficient balance $110", exception.getMessage());
        Assert.assertEquals(account.getStringBalance(), "$100");
        Mockito.verify(transactionRepo, Mockito.never()).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.never()).updateAccount(Mockito.any());
    }

    @Test
    public void testTransfer_transactionValid_thenSuccessDeductedSourceBalanceAndIncreaseDestBalance() throws Exception {
        Account sourceAccount = new Account(new Dollar(100), "Jane", "112233", "112211");
        Account destinationAccount = new Account(new Dollar(50), "John", "223344", "334422");
        serviceInTest.transfer(sourceAccount, destinationAccount, new Dollar(20), "REF123");
        Assert.assertEquals(sourceAccount.getStringBalance(), "$80");
        Assert.assertEquals(destinationAccount.getStringBalance(), "$70");
        Mockito.verify(transactionRepo, Mockito.times(1)).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.times(2)).updateAccount(Mockito.any());
    }

    @Test
    public void testTransfer_transactionInvalid_thenThrowException() throws Exception {
        Account sourceAccount = new Account(new Dollar(100), "Jane", "112233", "112211");
        Account destinationAccount = new Account(new Dollar(50), "John", "223344", "334422");
        Exception exception = Assert.assertThrows(Exception.class, () -> serviceInTest.transfer(sourceAccount, destinationAccount, new Dollar(110), "REF123"));
        Assert.assertEquals("Insufficient balance $110", exception.getMessage());
        Assert.assertEquals(sourceAccount.getStringBalance(), "$100");
        Assert.assertEquals(destinationAccount.getStringBalance(), "$50");
        Mockito.verify(transactionRepo, Mockito.never()).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.never()).updateAccount(Mockito.any());
    }
}