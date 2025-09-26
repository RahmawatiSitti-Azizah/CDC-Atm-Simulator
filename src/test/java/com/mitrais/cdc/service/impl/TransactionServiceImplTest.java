package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repo.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {
    private TransactionRepository repository = mock();
    private TransactionServiceImpl serviceInTest;

    @BeforeEach
    public void setUp() throws Exception {
        serviceInTest = new TransactionServiceImpl(repository);
    }

    @Test
    public void testGetTransactionHistoryAccount_whenThereAreTransaction_thenReturnListOfTransaction() {
        when(repository.findTransactionBySourceAccount(anyString())).thenReturn(List.of(mock(Transaction.class)));
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123");
        Assertions.assertTrue(result.size() == 1);
    }

    @Test
    public void testGetTransactionHistoryAccount_whenNoTransactionFound_thenReturnEmptyList() {
        when(repository.findTransactionBySourceAccount(anyString())).thenReturn(List.of());
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTransactionHistoryAccountWithSize_whenThereAreTransaction_thenReturnListOfTransaction() {
        when(repository.findLastTransactionBySourceAccount(anyString(), anyInt())).thenReturn(List.of(mock(Transaction.class)));
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123", 5);
        Assertions.assertTrue(result.size() == 1);
    }

    @Test
    public void testGetTransactionHistoryAccountWithSize_whenNoTransactionFound_thenReturnEmptyList() {
        when(repository.findLastTransactionBySourceAccount(anyString(), anyInt())).thenReturn(List.of());
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123", 5);
        Assertions.assertTrue(result.isEmpty());
    }

}