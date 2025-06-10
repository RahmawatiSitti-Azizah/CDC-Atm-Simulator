package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.TransactionValidationService;

class TransactionValidationServiceImpl implements TransactionValidationService {

    @Override
    public void withdrawAmount(Account account, long amount) throws Exception {
        if (amount % 10 != 0) {
            throw new Exception("Invalid amount");
        }
        if (amount > 1000) {
            throw new Exception("Maximum amount to transfer is $1000");
        }
        long balance = account.getBalance();
        if (account.getBalance() < amount) {
            throw new Exception("Insufficient balance $" + balance);
        }
    }

    @Override
    public long transferAmount(Account account, long amount) throws Exception {
        if (amount < 1) {
            throw new Exception("Minimum amount to transfer is $1");
        }
        if (amount > 1000) {
            throw new Exception("Maximum amount to transfer is $1000");
        }
        if (amount > account.getBalance()) {
            throw new Exception("Insufficient balance $" + amount);
        }
        return amount;
    }
}
