package com.mitrais.cdc.repo.impl;

import com.mitrais.cdc.repo.AccountRepository;
import com.mitrais.cdc.repo.TransactionRepository;

public class RepositoryFactory {
    private static AccountRepository accountRepositoryH2;
    private static TransactionRepository transactionRepository;

    public static AccountRepository createAccountRepository() {
        if (accountRepositoryH2 == null) {
            accountRepositoryH2 = new AccountRepositoryH2Impl();
        }
        return accountRepositoryH2;
    }

    public static TransactionRepository createTransactionRepository() {
        if (transactionRepository == null) {
            transactionRepository = new TransactionRepositoryImpl();
        }
        return transactionRepository;
    }
}
