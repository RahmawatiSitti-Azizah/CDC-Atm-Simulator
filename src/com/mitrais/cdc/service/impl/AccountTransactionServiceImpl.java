package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repo.AccountRepository;
import com.mitrais.cdc.repo.TransactionRepository;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.util.ReferenceNumberGenerator;

import java.util.Random;

class AccountTransactionServiceImpl implements AccountTransactionService {


    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public AccountTransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void withdraw(Account account, Money amount) throws Exception {
        account.decreaseBalance(amount);
        transactionRepository.saveTransaction(new Transaction(account, null, amount, ReferenceNumberGenerator.generateWithdrawRefnumber(new Random()), "Withdraw", null));
        accountRepository.updateAccount(account);
    }

    @Override
    public void transfer(Account sourceAccount, Account destinationAccount, Money amount, String referenceNumber) throws Exception {
        if (sourceAccount.getAccountNumber().equals(destinationAccount.getAccountNumber())) {
            throw new Exception("invalid transfer: destination account is the same as source account");
        }
        sourceAccount.decreaseBalance(amount);
        destinationAccount.increaseBalance(amount);
        transactionRepository.saveTransaction(new Transaction(sourceAccount, destinationAccount, amount, referenceNumber, "Transfer", null));
        accountRepository.updateAccount(sourceAccount);
        accountRepository.updateAccount(destinationAccount);
    }
}
