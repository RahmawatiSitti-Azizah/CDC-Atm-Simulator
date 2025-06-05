package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.StringMatcherUtil;

import javax.xml.bind.ValidationException;

class UserInputServiceImpl implements UserInputService {

    @Override
    public long toValidatedAmount(String input) throws ValidationException {
        if (!StringMatcherUtil.checkStringIsNumberOnly(input)) {
            throw new ValidationException("Invalid amount");
        }
        long amount = Long.parseLong(input);
        return amount;
    }

    @Override
    public int toValidatedMenu(String input) {
        return StringMatcherUtil.checkStringIsNumberWithLength(input, 1) ? Integer.parseInt(input) : 0;
    }
}
