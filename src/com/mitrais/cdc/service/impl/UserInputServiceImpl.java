package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.ErrorConstant;
import com.mitrais.cdc.util.StringMatcherUtil;

class UserInputServiceImpl implements UserInputService {

    @Override
    public Money toValidatedMoney(String input) throws Exception {
        if (!StringMatcherUtil.checkStringIsNumberOnly(input)) {
            throw new Exception(ErrorConstant.INVALID_AMOUNT);
        }
        long amount = Long.parseLong(input);
        return new Dollar(amount);
    }

    @Override
    public int toValidatedMenu(String input) {
        return StringMatcherUtil.checkStringIsNumberWithLength(input, 1) ? Integer.parseInt(input) : 0;
    }
}
