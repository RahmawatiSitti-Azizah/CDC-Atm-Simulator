package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.TransactionValidationService;

import javax.xml.bind.ValidationException;

class TransactionValidationServiceImpl implements TransactionValidationService {

    @Override
    public void withdrawAmount(Account account, long amount) throws ValidationException {
        if (amount % 10 != 0) {
            throw new ValidationException("Invalid amount");
        }
        if (amount > 1000) {
            throw new ValidationException("Maximum amount to transfer is $1000");
        }
        long balance = account.getBalance();
        if (account.getBalance() < amount) {
            throw new ValidationException("Insufficient balance $" + balance);
        }
    }

    @Override
    public long transferAmount(Account account, long amount) throws ValidationException {
        if (amount < 1) {
            throw new ValidationException("Minimum amount to transfer is $1");
        }
        if (amount > 1000) {
            throw new ValidationException("Maximum amount to transfer is $1000");
        }
        if (amount > account.getBalance()) {
            throw new ValidationException("Insufficient balance $" + amount);
        }
        return amount;
    }
}
