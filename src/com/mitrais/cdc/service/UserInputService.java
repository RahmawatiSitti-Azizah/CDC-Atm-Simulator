package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Money;

public interface UserInputService {

    public Money toValidatedMoney(String input) throws Exception;

    public int toValidatedMenu(String input);
}
