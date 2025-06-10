package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountTransactionService;

class AccountTransactionServiceImpl implements AccountTransactionService {

    @Override
    public void withdraw(Account account, long withdrawAmount) throws Exception {
        account.decreaseBalance(withdrawAmount);
    }

    @Override
    public void transfer(Account sourceAccount, Account destinationAccount, long transferAmount) throws Exception {
        sourceAccount.decreaseBalance(transferAmount);
        destinationAccount.increaseBalance(transferAmount);
    }
}
