package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

import javax.xml.bind.ValidationException;

public interface TransactionValidationService {
    void withdrawAmount(Account account, long amount) throws ValidationException;

    long transferAmount(Account account, long amount) throws ValidationException;
}
