package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.AccountTransactionService;

class AccountTransactionServiceImpl implements AccountTransactionService {

    @Override
    public void withdraw(Account account, Money withdrawCurrency) throws Exception {
        account.decreaseBalance(withdrawCurrency);
    }

    @Override
    public void transfer(Account sourceAccount, Account destinationAccount, Money amount) throws Exception {
        sourceAccount.decreaseBalance(amount);
        destinationAccount.increaseBalance(amount);
    }
}
