package com.mitrais.cdc.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

    private static Account getAccount() {
        Account account = new Account(new Dollar(100), "Prince", "112290", "123456");
        return account;
    }

    @Test
    public void testLoginTrue() {
        Account account = getAccount();
        Assertions.assertTrue(account.login("112290", "123456"));
    }

    @Test
    public void testLoginFalseAccountNumber() {
        Account account = getAccount();
        Assertions.assertFalse(account.login("112288", "123456"));
    }

    @Test
    public void testLoginFalsePin() {
        Account account = getAccount();
        Assertions.assertFalse(account.login("112290", "123123"));
    }

    @Test
    public void testLoginNullUsername() {
        Account account = getAccount();
        Assertions.assertFalse(account.login(null, "123456"));
    }

    @Test
    public void testLoginNullPin() {
        Account account = getAccount();
        Assertions.assertFalse(account.login("Prince", null));
    }
}