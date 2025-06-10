package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

public interface TransactionValidationService {
    void withdrawAmount(Account account, long amount) throws Exception;

    long transferAmount(Account account, long amount) throws Exception;
}
