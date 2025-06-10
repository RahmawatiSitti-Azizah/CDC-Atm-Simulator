package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.SearchAccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class SearchAccountServiceImpl implements SearchAccountService {
    private static final List<Account> accounts = new ArrayList<>();

    public SearchAccountServiceImpl() {
        accounts.add(new Account(100, "John Doe", "112233", "012108"));
        accounts.add(new Account(30, "Jane Doe", "112244", "932012"));
    }

    @Override
    public Account get(String accountNumber, String pin) throws Exception {
        try {
            Account searchResult = accounts.stream().filter((Account account) -> account.login(accountNumber, pin)).findAny().get();
            return searchResult;
        } catch (NoSuchElementException e) {
            throw new Exception("Invalid Account Number/PIN");
        }
    }

    @Override
    public Account getByID(String accountNumber) throws Exception {
        try {
            Account searchResult = accounts.stream().filter((Account account) -> account.getAccountNumber().equals(accountNumber)).findAny().get();
            return searchResult;
        } catch (NoSuchElementException e) {
            throw new Exception("Invalid Account");
        }
    }
}
