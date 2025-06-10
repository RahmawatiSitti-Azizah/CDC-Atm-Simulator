package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountTransactionService;

class AccountTransactionServiceImpl implements AccountTransactionService {

    @Override
    public void withdraw(Account account, long withdrawAmount) {
        try {
            account.decreaseBalance(withdrawAmount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void transfer(Account sourceAccount, Account destinationAccount, long transferAmount) {
        try {
            sourceAccount.decreaseBalance(transferAmount);
            destinationAccount.increaseBalance(transferAmount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
