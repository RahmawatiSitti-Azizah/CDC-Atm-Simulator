package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import com.mitrais.cdc.service.TransactionService;

import java.util.List;

class TransactionServiceImpl implements TransactionService {
    @Override
    public List<Transaction> getTransactionHistoryAccount(String accountNumber) {
        return RepositoryFactory.createTransactionRepository().findTransactionBySourceAccount(accountNumber);
    }

    @Override
    public List<Transaction> getTransactionHistoryAccount(String accountNumber, int size) {
        return RepositoryFactory.createTransactionRepository().findLastTransactionBySourceAccount(accountNumber, size);
    }
}
