package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.StringMatcherUtil;

class UserInputServiceImpl implements UserInputService {

    @Override
    public long toValidatedAmount(String input) throws Exception {
        if (!StringMatcherUtil.checkStringIsNumberOnly(input)) {
            throw new Exception("Invalid amount");
        }
        long amount = Long.parseLong(input);
        return amount;
    }

    @Override
    public int toValidatedMenu(String input) {
        return StringMatcherUtil.checkStringIsNumberWithLength(input, 1) ? Integer.parseInt(input) : 0;
    }
}
