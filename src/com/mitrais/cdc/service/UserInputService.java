package com.mitrais.cdc.service;

public interface UserInputService {

    public long toValidatedAmount(String input) throws Exception;

    public int toValidatedMenu(String input);
}
