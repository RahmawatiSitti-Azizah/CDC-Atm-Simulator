package com.mitrais.cdc.service;

import javax.xml.bind.ValidationException;

public interface UserInputService {

    public long toValidatedAmount(String input) throws ValidationException;

    public int toValidatedMenu(String input);
}
