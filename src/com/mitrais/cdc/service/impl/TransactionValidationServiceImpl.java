package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.service.TransactionValidationService;

class TransactionValidationServiceImpl implements TransactionValidationService {

    @Override
    public void validateWithdrawAmount(long amount) throws Exception {
        if (amount % 10 != 0) {
            throw new Exception("Invalid amount");
        }
        if (amount > 1000) {
            throw new Exception("Maximum amount to transfer is $1000");
        }
    }

    @Override
    public long validateTransferAmount(long amount) throws Exception {
        if (amount < 1) {
            throw new Exception("Minimum amount to transfer is $1");
        }
        if (amount > 1000) {
            throw new Exception("Maximum amount to transfer is $1000");
        }
        return amount;
    }
}
