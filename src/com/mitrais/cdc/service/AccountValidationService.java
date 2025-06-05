package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

import javax.xml.bind.ValidationException;

public interface AccountValidationService {
    public void accountNumber(String accountNumber) throws ValidationException;

    public void pin(String pin) throws ValidationException;

    public Account toValidatedAccount(String accountNumber) throws ValidationException;
}
