package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Money;

public interface TransactionAmountValidatorService {
    void validateWithdrawAmount(Money money) throws Exception;

    void validateTransferAmount(Money amount) throws Exception;
}
