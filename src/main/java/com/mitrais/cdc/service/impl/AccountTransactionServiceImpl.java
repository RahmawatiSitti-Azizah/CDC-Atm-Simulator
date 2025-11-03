package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.dto.TransactionDto;
import com.mitrais.cdc.model.mapper.TransactionMapper;
import com.mitrais.cdc.repository.AccountRepository;
import com.mitrais.cdc.repository.TransactionRepository;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.util.ErrorConstant;
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
    public TransactionDto withdraw(AccountDto account, Money amount) throws Exception {
        Account sourceAccount = accountRepository.findAccountByAccountNumber(account.getAccountNumber());
        sourceAccount.decreaseBalance(amount);
        Transaction transaction = transactionRepository.save(new Transaction(null, sourceAccount, null, amount, ReferenceNumberGenerator.generateWithdrawRefnumber(new Random()), "Withdraw", LocalDateTime.now()));
        accountRepository.save(sourceAccount);
        account.setBalance(sourceAccount.getBalance());
        return TransactionMapper.toTransactionDto(transaction);
    }

    @Override
    public TransactionDto transfer(AccountDto source, AccountDto destination, Money amount, String referenceNumber) throws Exception {
        Account sourceAccount = accountRepository.findAccountByAccountNumber(source.getAccountNumber());
        Account destinationAccount = accountRepository.findAccountByAccountNumber(destination.getAccountNumber());
        if (source.getAccountNumber().equals(destination.getAccountNumber())) {
            throw new Exception(ErrorConstant.DESTINATION_ACCOUNT_IS_THE_SAME_AS_SOURCE_ACCOUNT);
        }
        sourceAccount.decreaseBalance(amount);
        destinationAccount.increaseBalance(amount);
        Transaction transaction = transactionRepository.save(new Transaction(null, sourceAccount, destinationAccount, amount, referenceNumber, "Transfer", LocalDateTime.now()));
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        return TransactionMapper.toTransactionDto(transaction);
    }
}
