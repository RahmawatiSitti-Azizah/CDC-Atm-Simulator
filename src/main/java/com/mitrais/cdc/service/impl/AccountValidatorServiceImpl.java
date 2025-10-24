package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.util.ErrorConstant;
import com.mitrais.cdc.util.StringMatcherUtil;
import org.springframework.stereotype.Service;

@Service
class AccountValidatorServiceImpl implements AccountValidatorService {

    @Override
    public void validateAccountNumber(String accountNumber, String errorMessage) throws Exception {
        if (!StringMatcherUtil.checkStringIsNumberOnly(accountNumber)) {
            throw new Exception(errorMessage);
        }
        validateLength(accountNumber, 6, ErrorConstant.ACCOUNT_NUMBER_SHOULD_HAVE_6_DIGITS_LENGTH);
    }

    @Override
    public void validatePin(String pin) throws Exception {
        validateLength(pin, 6, "Pin should have 6 digits length");
        if (!StringMatcherUtil.checkStringIsNumberWithLength(pin, 6)) {
            throw new Exception("Pin should only contains numbers");
        }
    }

    private static void validateLength(String input, int maxLength, String errorMessage) throws Exception {
        if (input != null && input.length() > 6 || input.length() < 6) {
            throw new Exception(errorMessage);
        }
    }
}
