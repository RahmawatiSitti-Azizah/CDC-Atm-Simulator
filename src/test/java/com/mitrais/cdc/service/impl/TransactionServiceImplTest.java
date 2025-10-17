package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
        when(repository.findBySourceAccountAccountNumber(anyString())).thenReturn(List.of(mock(Transaction.class)));
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123");
        Assertions.assertTrue(result.size() == 1);
    }

    @Test
    public void testGetTransactionHistoryAccount_whenNoTransactionFound_thenReturnEmptyList() {
        when(repository.findBySourceAccountAccountNumber(anyString())).thenReturn(List.of());
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTransactionHistoryAccountWithSize_whenThereAreTransaction_thenReturnListOfTransaction() {
        Page page = mock(Page.class);
        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(page.getContent()).thenReturn(List.of(mock(Transaction.class)));
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123", 5);
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    public void testGetTransactionHistoryAccountWithSize_whenNoTransactionFound_thenReturnEmptyList() {
        Page page = mock(Page.class);
        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(page.getContent()).thenReturn(List.of());
        List<Transaction> result = serviceInTest.getTransactionHistoryAccount("123123", 5);
        Assertions.assertTrue(result.isEmpty());
    }

}