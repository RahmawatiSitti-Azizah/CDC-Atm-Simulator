package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;

public interface AccountTransactionService {
    /**
     * Service to withdraw amount from account.
     *
     * @param account source account to be withdrawn.
     * @param amount  the amount to be withdrawn (not null)
     * @throws Exception if there is any issue occur
     */
    public void withdraw(Account account, Money amount) throws Exception;

    /**
     * Service to transfer xxx amount from source account to destination account.
     *
     * @param sourceAccount      transfer source account (not null)
     * @param destinationAccount transfer destination account (not null)
     * @param amount             the amount to be transferred (not null)
     * @param referenceNumber
     * @throws Exception if there is any issue occur
     */
    public void transfer(Account sourceAccount, Account destinationAccount, Money amount, String referenceNumber) throws Exception;
}
