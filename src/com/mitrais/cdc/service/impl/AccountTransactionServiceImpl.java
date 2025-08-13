package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import com.mitrais.cdc.service.AccountTransactionService;

class AccountTransactionServiceImpl implements AccountTransactionService {

    @Override
    public void withdraw(Account account, Money amount) throws Exception {
        account.decreaseBalance(amount);
        RepositoryFactory.createTransactionRepository().saveTransaction(new Transaction(account, null, amount, null, "Withdraw"));
        RepositoryFactory.createAccountRepository().updateAccount(account);
    }

    @Override
    public void transfer(Account sourceAccount, Account destinationAccount, Money amount, String referenceNumber) throws Exception {
        sourceAccount.decreaseBalance(amount);
        destinationAccount.increaseBalance(amount);
        RepositoryFactory.createTransactionRepository().saveTransaction(new Transaction(sourceAccount, destinationAccount, amount, referenceNumber, "Transfer"));
        RepositoryFactory.createAccountRepository().updateAccount(sourceAccount);
        RepositoryFactory.createAccountRepository().updateAccount(destinationAccount);
    }
}
