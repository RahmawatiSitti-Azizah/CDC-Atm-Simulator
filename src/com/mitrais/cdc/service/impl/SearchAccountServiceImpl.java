package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.util.ErrorConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class SearchAccountServiceImpl implements SearchAccountService {
    private static final List<Account> accounts = new ArrayList<>();

    public SearchAccountServiceImpl() {
    }

    public void addAccount(Money initialBalance, String accountHolderName, String accountNumber, String pin) {
        Account newAccount = new Account(initialBalance, accountHolderName, accountNumber, pin);
        try {
            Account account = getByID(accountNumber);
            if (account != null) {
                if (!account.equals(newAccount)) {
                    System.out.println(ErrorConstant.DUPLICATE_ACCOUNT_NUMBER + accountNumber);
                } else {
                    System.out.println(ErrorConstant.DUPLICATE_RECORDS + newAccount.toString());
                }
            }
        } catch (Exception e) {
            accounts.add(newAccount);
        }
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
