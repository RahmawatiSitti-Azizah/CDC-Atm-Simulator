package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.dto.TransactionDto;

public interface AccountTransactionService {
    /**
     * Service to withdraw amount from account.
     *
     * @param account source account to be withdrawn.
     * @param amount  the amount to be withdrawn (not null)
     * @return
     * @throws Exception if there is any issue occur
     */
    public TransactionDto withdraw(AccountDto account, Money amount) throws Exception;

    /**
     * Service to transfer xxx amount from source account to destination account.
     *
     * @param sourceAccount      transfer source account (not null)
     * @param destinationAccount transfer destination account (not null)
     * @param amount             the amount to be transferred (not null)
     * @param referenceNumber
     * @return
     * @throws Exception if there is any issue occur
     */
    public TransactionDto transfer(AccountDto sourceAccount, AccountDto destinationAccount, Money amount, String referenceNumber) throws Exception;
}
