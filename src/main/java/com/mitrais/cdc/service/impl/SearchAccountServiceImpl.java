package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.repository.AccountRepository;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.util.ErrorConstant;
import org.springframework.stereotype.Service;

@Service
class SearchAccountServiceImpl implements SearchAccountService {
    private final AccountRepository accountRepository;

    public SearchAccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
            accountRepository.save(newAccount);
        }
    }

    @Override
    public Account get(String accountNumber, String pin) throws Exception {

        Account searchResult = accountRepository.findAccountByAccountNumber(accountNumber);
        if (searchResult == null || !searchResult.login(accountNumber, pin)) {
            throw new Exception("Invalid Account Number/PIN");
        }
        return searchResult;
    }

    @Override
    public Account getByID(String accountNumber) throws Exception {
        Account searchResult = accountRepository.findAccountByAccountNumber(accountNumber);
        if (searchResult == null) {
            throw new Exception("Invalid Account");
        }
        return searchResult;
    }
}
