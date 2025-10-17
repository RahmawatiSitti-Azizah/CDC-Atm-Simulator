package com.mitrais.cdc.repo.impl;

import com.mitrais.cdc.repo.AccountRepositoryH2;
import com.mitrais.cdc.repo.TransactionRepositoryH2;

public class RepositoryFactory {
    private static AccountRepositoryH2 accountRepositoryH2;
    private static TransactionRepositoryH2 transactionRepositoryH2;

    public static AccountRepositoryH2 createAccountRepository() {
        if (accountRepositoryH2 == null) {
            accountRepositoryH2 = new AccountRepositoryH2Impl();
        }
        return accountRepositoryH2;
    }

    public static TransactionRepositoryH2 createTransactionRepository() {
        if (transactionRepositoryH2 == null) {
            transactionRepositoryH2 = new TransactionRepositoryImpl();
        }
        return transactionRepositoryH2;
    }
}
