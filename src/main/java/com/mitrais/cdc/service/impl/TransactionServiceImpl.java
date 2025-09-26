package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repo.TransactionRepository;
import com.mitrais.cdc.service.TransactionService;

import java.util.List;

class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getTransactionHistoryAccount(String accountNumber) {
        return transactionRepository.findTransactionBySourceAccount(accountNumber);
    }

    @Override
    public List<Transaction> getTransactionHistoryAccount(String accountNumber, int size) {
        return transactionRepository.findLastTransactionBySourceAccount(accountNumber, size);
    }
}
