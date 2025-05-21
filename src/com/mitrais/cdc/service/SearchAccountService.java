package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

public interface SearchAccountService {
    public Account getLoginAccount(String accountNumber, String pin) throws RuntimeException;

    public Account findDestinationAccount(String accountNumber) throws RuntimeException;
}
