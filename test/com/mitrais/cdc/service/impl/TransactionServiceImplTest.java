package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repo.TransactionRepository;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {
    private TransactionRepository repository = mock();
    private MockedStatic<RepositoryFactory> mockRepositoryFactory;
    private TransactionServiceImpl serviceInTest;

    @Before
    public void setUp() throws Exception {
        mockRepositoryFactory = Mockito.mockStatic(RepositoryFactory.class);
        mockRepositoryFactory.when(RepositoryFactory::createTransactionRepository).thenReturn(repository);
        serviceInTest = new TransactionServiceImpl();
    }

    @After
    public void tearDown() throws Exception {
        mockRepositoryFactory.close();
    }

    @Test
    public void testGetTransactionHistoryAccount_whenThereAreTransaction_thenReturnListOfTransaction() {
        when(repository.findTransactionBySourceAccount(anyString())).thenReturn(List.of(mock(Transaction.class)));
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123");
        Assert.assertTrue(result.size() == 1);
    }

    @Test
    public void testGetTransactionHistoryAccount_whenNoTransactionFound_thenReturnEmptyList() {
        when(repository.findTransactionBySourceAccount(anyString())).thenReturn(List.of());
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123");
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTransactionHistoryAccountWithSize_whenThereAreTransaction_thenReturnListOfTransaction() {
        when(repository.findLastTransactionBySourceAccount(anyString(), anyInt())).thenReturn(List.of(mock(Transaction.class)));
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123", 5);
        Assert.assertTrue(result.size() == 1);
    }

    @Test
    public void testGetTransactionHistoryAccountWithSize_whenNoTransactionFound_thenReturnEmptyList() {
        when(repository.findLastTransactionBySourceAccount(anyString(), anyInt())).thenReturn(List.of());
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123", 5);
        Assert.assertTrue(result.isEmpty());
    }
    
}