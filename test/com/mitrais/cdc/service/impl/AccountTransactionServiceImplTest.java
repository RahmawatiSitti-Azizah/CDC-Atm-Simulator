package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.AccountRepository;
import com.mitrais.cdc.repo.TransactionRepository;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class AccountTransactionServiceImplTest {
    private AccountTransactionServiceImpl serviceInTest = new AccountTransactionServiceImpl();
    private MockedStatic<RepositoryFactory> mockRepoFactory;

    @Before
    public void setUp() throws Exception {
        mockRepoFactory = Mockito.mockStatic(RepositoryFactory.class);
    }

    @After
    public void tearDown() throws Exception {
        mockRepoFactory.close();
    }

    @Test
    public void testWithdraw_transactionValid_thenBalanceDeducted() throws Exception {
        TransactionRepository transactionRepo = Mockito.mock(TransactionRepository.class);
        mockRepoFactory.when(RepositoryFactory::createTransactionRepository).thenReturn(transactionRepo);
        AccountRepository accountRepo = Mockito.mock(AccountRepository.class);
        mockRepoFactory.when(RepositoryFactory::createAccountRepository).thenReturn(accountRepo);
        Account account = new Account(new Dollar(100), "Jane", "112233", "112211");
        serviceInTest.withdraw(account, new Dollar(20));
        Assert.assertEquals(account.getStringBalance(), "$80");
        Mockito.verify(transactionRepo, Mockito.times(1)).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.times(1)).updateAccount(Mockito.any());
    }

    @Test
    public void testWithdraw_transactionAmountExceedAccountBalance_thenThrowException() throws Exception {
        TransactionRepository transactionRepo = Mockito.mock(TransactionRepository.class);
        mockRepoFactory.when(RepositoryFactory::createTransactionRepository).thenReturn(transactionRepo);
        AccountRepository accountRepo = Mockito.mock(AccountRepository.class);
        mockRepoFactory.when(RepositoryFactory::createAccountRepository).thenReturn(accountRepo);
        Account account = new Account(new Dollar(100), "Jane", "112233", "112211");
        Exception exception = Assert.assertThrows(Exception.class, () -> serviceInTest.withdraw(account, new Dollar(110)));
        Assert.assertEquals("Insufficient balance $110", exception.getMessage());
        Assert.assertEquals(account.getStringBalance(), "$100");
        Mockito.verify(transactionRepo, Mockito.never()).saveTransaction(Mockito.any());
        Mockito.verify(accountRepo, Mockito.never()).updateAccount(Mockito.any());
    }

    @Test
    public void testTransfer_transactionValid_thenSuccessDeductedSourceBalanceAndIncreaseDestBalance() throws Exception {
        TransactionRepository transactionRepo = Mockito.mock(TransactionRepository.class);
        mockRepoFactory.when(RepositoryFactory::createTransactionRepository).thenReturn(transactionRepo);
        AccountRepository accountRepo = Mockito.mock(AccountRepository.class);
        mockRepoFactory.when(RepositoryFactory::createAccountRepository).thenReturn(accountRepo);
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
        TransactionRepository transactionRepo = Mockito.mock(TransactionRepository.class);
        mockRepoFactory.when(RepositoryFactory::createTransactionRepository).thenReturn(transactionRepo);
        AccountRepository accountRepo = Mockito.mock(AccountRepository.class);
        mockRepoFactory.when(RepositoryFactory::createAccountRepository).thenReturn(accountRepo);
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