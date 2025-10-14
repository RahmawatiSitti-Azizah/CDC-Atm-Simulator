package com.mitrais.cdc.repo;

import com.mitrais.cdc.model.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository {
    public List<Account> getAllAccount();

    public Account getAccountByAccountNumber(String accountNumber);

    public Account saveAccount(Account account);

    public Account updateAccount(Account account);
}
