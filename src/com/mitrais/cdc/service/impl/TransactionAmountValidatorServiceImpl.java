package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.TransactionAmountValidatorService;

class TransactionAmountValidatorServiceImpl implements TransactionAmountValidatorService {

    @Override
    public void validateWithdrawAmount(Money money) throws Exception {
        if (!money.isMultipleOf(10)) {
            throw new Exception("Invalid amount");
        }
        if (money.isMoreThan(new Dollar(1000))) {
            throw new Exception("Maximum amount to transfer is $1000");
        }
    }

    @Override
    public void validateTransferAmount(Money amount) throws Exception {
        if (!amount.isMoreThanOrEquals(new Dollar(1))) {
            throw new Exception("Minimum amount to transfer is $1");
        }
        if (amount.isMoreThan(new Dollar(1000))) {
            throw new Exception("Maximum amount to transfer is $1000");
        }
    }
}
