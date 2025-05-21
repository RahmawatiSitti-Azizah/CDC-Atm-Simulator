package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

import javax.xml.bind.ValidationException;

public interface AccountValidationService {
    public void validateAccountNumber (String accountNumber) throw ValidationException;
    public void validateAccount(Account account) throws ValidationException;
}
