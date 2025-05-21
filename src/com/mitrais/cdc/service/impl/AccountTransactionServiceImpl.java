package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.AccountTransactionService;

import javax.xml.bind.ValidationException;

class AccountTransactionServiceImpl implements AccountTransactionService {

    @Override
    public void withdraw(Account account, Money withdrawAmount) throws ValidationException {
        
    }

    @Override
    public void transfer(Account sourceAccount, Account destinationAccount, Money transferAmount) throws ValidationException {

    }
}
