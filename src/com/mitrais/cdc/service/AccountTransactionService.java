package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

import javax.xml.bind.ValidationException;

public interface AccountTransactionService {
    /**
     * Service to withdraw amount from account.
     *
     * @param account        source account to be withdrawn.
     * @param withdrawAmount withdraw amount
     * @throws RuntimeException if there is any issue occur
     */
    public void withdraw(Account account, long withdrawAmount) throws ValidationException;

    /**
     * Service to transfer xxx amount from source account to destination account.
     *
     * @param sourceAccount      transfer source account (not null)
     * @param destinationAccount transfer destination account (not null)
     * @param transferAmount     transfer amount (not null)
     * @throws RuntimeException if there is any issue occur
     */
    public void transfer(Account sourceAccount, Account destinationAccount, long transferAmount);
}
