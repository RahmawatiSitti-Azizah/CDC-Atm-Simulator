package com.mitrais.cdc.service;

public interface AccountValidatorService {
    public void validateAccountNumber(String accountNumber, String errorMessage) throws Exception;

    public void validatePin(String pin) throws Exception;
}
