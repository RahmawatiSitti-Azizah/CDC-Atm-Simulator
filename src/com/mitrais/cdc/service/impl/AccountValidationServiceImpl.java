package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountValidationService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.util.StringMatcherUtil;

import javax.xml.bind.ValidationException;

class AccountValidationServiceImpl implements AccountValidationService {
    private SearchAccountService searchService = ServiceFactory.createSearchAccountValidationService();

    @Override
    public void accountNumber(String accountNumber) throws ValidationException {
        if (!StringMatcherUtil.checkStringIsNumberOnly(accountNumber)) {
            throw new ValidationException("Account Number should only contains numbers");
        }
        validateAccountNumberLength(accountNumber, "Account Number should have 6 digits length");
    }

    private static void validateAccountNumberLength(String accountNumber, String message) throws ValidationException {
        int length = accountNumber != null ? accountNumber.length() : 0;
        if (length > 6 || length < 6) {
            throw new ValidationException(message);
        }
    }

    @Override
    public void pin(String pin) throws ValidationException {
        validateAccountNumberLength(pin, "Pin should have 6 digits length");
        if (!StringMatcherUtil.checkStringIsNumberWithLength(pin, 6)) {
            throw new ValidationException("Pin should only contains numbers");
        }
    }

    @Override
    public Account toValidatedAccount(String accountNumber) throws ValidationException {
        if (!StringMatcherUtil.checkStringIsNumberOnly(accountNumber)) {
            throw new ValidationException("Invalid Account");
        }
        validateAccountNumberLength(accountNumber, "Account Number should have 6 digits length");
        return searchService.getByID(accountNumber);
    }
}
