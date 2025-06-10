package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountValidationService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.util.StringMatcherUtil;

class AccountValidationServiceImpl implements AccountValidationService {
    private SearchAccountService searchService = ServiceFactory.createSearchAccountValidationService();

    @Override
    public void accountNumber(String accountNumber) throws Exception {
        if (!StringMatcherUtil.checkStringIsNumberOnly(accountNumber)) {
            throw new Exception("Account Number should only contains numbers");
        }
        validateAccountNumberLength(accountNumber, "Account Number should have 6 digits length");
    }

    private static void validateAccountNumberLength(String accountNumber, String message) throws Exception {
        int length = accountNumber != null ? accountNumber.length() : 0;
        if (length > 6 || length < 6) {
            throw new Exception(message);
        }
    }

    @Override
    public void pin(String pin) throws Exception {
        validateAccountNumberLength(pin, "Pin should have 6 digits length");
        if (!StringMatcherUtil.checkStringIsNumberWithLength(pin, 6)) {
            throw new Exception("Pin should only contains numbers");
        }
    }

    @Override
    public Account searchAccount(String accountNumber) throws Exception {
        if (!StringMatcherUtil.checkStringIsNumberOnly(accountNumber)) {
            throw new Exception("Invalid Account");
        }
        validateAccountNumberLength(accountNumber, "Account Number should have 6 digits length");
        return searchService.getByID(accountNumber);
    }
}
