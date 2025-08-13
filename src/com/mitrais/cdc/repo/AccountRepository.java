package com.mitrais.cdc.repo;

import com.mitrais.cdc.model.Account;

import java.util.List;

public interface AccountRepository {
    public List<Account> getAllAccount();

    public Account getAccountByAccountNumber(String accountNumber);

    public Account saveAccount(Account account);

    public Account updateAccount(Account account);
}
