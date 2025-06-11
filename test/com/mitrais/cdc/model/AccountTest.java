package com.mitrais.cdc.model;

import junit.framework.TestCase;

public class AccountTest extends TestCase {

    private static Account getAccount() {
        Account account = new Account(new Dollar(100), "Prince", "112290", "123456");
        return account;
    }

    public void testLoginTrue() {
        Account account = getAccount();
        assertTrue(account.login("112290", "123456"));
    }

    public void testLoginFalseAccountNumber() {
        Account account = getAccount();
        assertFalse(account.login("112288", "123456"));
    }

    public void testLoginFalsePin() {
        Account account = getAccount();
        assertFalse(account.login("112290", "123123"));
    }

    public void testLoginNullUsername() {
        Account account = getAccount();
        assertFalse(account.login(null, "123456"));
    }

    public void testLoginNullPin() {
        Account account = getAccount();
        assertFalse(account.login("Prince", null));
    }
}