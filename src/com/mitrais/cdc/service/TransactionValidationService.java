package com.mitrais.cdc.service;

public interface TransactionValidationService {
    void validateWithdrawAmount(long amount) throws Exception;

    long validateTransferAmount(long amount) throws Exception;
}
