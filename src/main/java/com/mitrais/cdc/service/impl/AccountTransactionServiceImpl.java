package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repository.AccountRepository;
import com.mitrais.cdc.repository.TransactionRepository;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.util.ReferenceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
class AccountTransactionServiceImpl implements AccountTransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    @Autowired
    public AccountTransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void withdraw(Account account, Money amount) throws Exception {
        account.decreaseBalance(amount);
        transactionRepository.save(new Transaction(null, account, null, amount, ReferenceNumberGenerator.generateWithdrawRefnumber(new Random()), "Withdraw", LocalDateTime.now()));
        accountRepository.save(account);
    }

    @Override
    public void transfer(Account sourceAccount, Account destinationAccount, Money amount, String referenceNumber) throws Exception {
        if (sourceAccount.getAccountNumber().equals(destinationAccount.getAccountNumber())) {
            throw new Exception("invalid transfer: destination account is the same as source account");
        }
        sourceAccount.decreaseBalance(amount);
        destinationAccount.increaseBalance(amount);
        transactionRepository.save(new Transaction(null, sourceAccount, destinationAccount, amount, referenceNumber, "Transfer", null));
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }
}
