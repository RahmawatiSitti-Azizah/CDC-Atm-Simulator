package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.TransactionAmountValidatorService;
import com.mitrais.cdc.util.ErrorConstant;

class TransactionAmountValidatorServiceImpl implements TransactionAmountValidatorService {

    @Override
    public void validateWithdrawAmount(Money money) throws Exception {
        if (!money.isMultipleOf(10)) {
            throw new Exception(ErrorConstant.INVALID_AMOUNT);
        }
        if (money.isMoreThan(new Dollar(1000))) {
            throw new Exception(ErrorConstant.MAXIMUM_TRANSACTION_AMOUNT_EXCEED);
        }
    }

    @Override
    public void validateTransferAmount(Money amount) throws Exception {
        if (!amount.isMoreThanOrEquals(new Dollar(1))) {
            throw new Exception("Minimum amount to transfer is $1");
        }
        if (amount.isMoreThan(new Dollar(1000))) {
            throw new Exception(ErrorConstant.MAXIMUM_TRANSACTION_AMOUNT_EXCEED);
        }
    }
}
