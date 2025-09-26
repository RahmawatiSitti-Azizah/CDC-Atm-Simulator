package com.mitrais.cdc.repo.impl;

import com.mitrais.cdc.repo.AccountRepository;
import com.mitrais.cdc.repo.TransactionRepository;

public class RepositoryFactory {
    private static AccountRepository accountRepository;
    private static TransactionRepository transactionRepository;

    public static AccountRepository createAccountRepository() {
        if (accountRepository == null) {
            accountRepository = new AccountRepositoryImpl();
        }
        return accountRepository;
    }

    public static TransactionRepository createTransactionRepository() {
        if (transactionRepository == null) {
            transactionRepository = new TransactionRepositoryImpl();
        }
        return transactionRepository;
    }
}
