package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

public interface AccountValidationService {
    public void accountNumber(String accountNumber) throws Exception;

    public void pin(String pin) throws Exception;

    public Account searchAccount(String accountNumber) throws Exception;
}
