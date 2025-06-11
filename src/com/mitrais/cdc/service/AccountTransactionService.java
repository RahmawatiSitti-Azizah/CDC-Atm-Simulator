package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;

public interface AccountTransactionService {
    /**
     * Service to withdraw amount from account.
     *
     * @param account  source account to be withdrawn.
     * @param currency
     * @throws RuntimeException if there is any issue occur
     */
    public void withdraw(Account account, Money currency) throws Exception;

    /**
     * Service to transfer xxx amount from source account to destination account.
     *
     * @param sourceAccount      transfer source account (not null)
     * @param destinationAccount transfer destination account (not null)
     * @param amount
     * @throws RuntimeException if there is any issue occur
     */
    public void transfer(Account sourceAccount, Account destinationAccount, Money amount) throws Exception;
}
