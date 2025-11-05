package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.model.dto.TransactionDto;
import com.mitrais.cdc.repository.TransactionRepository;
import com.mitrais.cdc.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {
    private TransactionRepository repository = mock();
    private TransactionServiceImpl serviceInTest;

    public static TransactionService getTransactionService(TransactionRepository transactionRepository) {
        return new TransactionServiceImpl(transactionRepository);
    }

    @BeforeEach
    public void setUp() throws Exception {
        serviceInTest = new TransactionServiceImpl(repository);
    }

    @Test
    public void testGetTransactionHistoryAccount_whenThereAreTransaction_thenReturnListOfTransaction() {
        Account sourceAccount = new Account("112233", "TEST 01", "123123", new Dollar(100.0));
        Account destinationAccount = new Account("112244", "TEST 02", "123123", new Dollar(130.0));
        Transaction transaction = new Transaction(1, sourceAccount, destinationAccount, new Dollar(40.0), "T501312", "Transfer", LocalDateTime.now());
        when(repository.findBySourceAccountAccountNumber(anyString())).thenReturn(List.of(transaction));
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
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        Account sourceAccount = new Account("112233", "TEST 01", "123123", new Dollar(100.0));
        Account destinationAccount = new Account("112244", "TEST 02", "123123", new Dollar(130.0));
        Transaction transaction = new Transaction(1, sourceAccount, destinationAccount, new Dollar(40.0), "T501312", "Transfer", LocalDateTime.now());
        when(page.getContent()).thenReturn(List.of(transaction));
        List<TransactionDto> result = serviceInTest.getTransactionHistoryAccount("123123", 5);
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    public void testGetTransactionHistoryAccountWithSize_whenNoTransactionFound_thenReturnEmptyList() {
        Page page = mock(Page.class);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(page.getContent()).thenReturn(List.of());
        List<TransactionDto> result = serviceInTest.getTransactionHistoryAccount("123123", 5);
        Assertions.assertTrue(result.isEmpty());
    }

}