package com.mitrais.cdc.repo;

import com.mitrais.cdc.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    public List<Transaction> findAllTransaction();

    public List<Transaction> findTransactionBySourceAccount(String sourceAccountNumber);

    public Transaction findTransactionByReferenceNumber(String referenceNumber);

    public Transaction saveTransaction(Transaction transaction);

    public List<Transaction> findLastTransactionBySourceAccount(String sourceAccountNumber, int size);
}
