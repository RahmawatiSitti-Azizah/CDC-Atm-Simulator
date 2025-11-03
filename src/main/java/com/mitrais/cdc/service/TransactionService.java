package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.model.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    /**
     * Service to get all transaction history of an account.
     *
     * @param accountNumber the account number to get the transaction history
     *                      (not null, must be 6 characters long)
     * @return a list of transactions associated with the account
     * (not null, may be empty if no transactions found)
     */
    public List<Transaction> getTransactionHistoryAccount(String accountNumber);

    /**
     * Service to get a limited size of last transaction history of an account.
     *
     * @param accountNumber the account number to get the transaction history
     *                      (not null, must be 6 characters long)
     * @param size          the limit size of transaction history to be returned,
     *                      return all transaction if all transaction is less than size
     *                      (must be greater than 0)
     * @return a list of transactions associated with the account
     * (not null, may be empty if no transactions found)
     */
    public List<TransactionDto> getTransactionHistoryAccount(String accountNumber, int size);
}
