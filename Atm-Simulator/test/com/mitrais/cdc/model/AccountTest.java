package com.mitrais.cdc.model;

import junit.framework.TestCase;

public class AccountTest extends TestCase {

    public void testLoginTrue() {
        Account account = new Account(100, "Prince", "112290", "123456");
        assertTrue(account.login("112290", "123456"));
    }

    public void testLoginFalseAccountNumber() {
        Account account = new Account(100, "Prince", "112290", "123456");
        assertFalse(account.login("112288", "123456"));
    }

    public void testLoginFalsePin() {
        Account account = new Account(100, "Prince", "112290", "123456");
        assertFalse(account.login("112290", "123123"));
    }

    public void testLoginNullUsername() {
        Account account = new Account(100, "Prince", "112290", "123456");
        assertFalse(account.login(null, "123456"));
    }

    public void testLoginNullPin() {
        Account account = new Account(100, "Prince", "112290", "123456");
        assertFalse(account.login("Prince", null));
    }

    public void testGetBalance() {
        Account account = new Account(50, "Prince", "112290", "123456");
        assertEquals(50.0, account.getBalance());
    }

    public void testGetAccountNumber() {
        Account account = new Account(50, "Prince", "112290", "123456");
        assertEquals("112290", account.getAccountNumber());
    }

    public void testSetBalance() {
        Account account = new Account(50, "Prince", "112290", "123456");
        assertEquals(50.0, account.getBalance());
        account.setBalance(80);
        assertEquals(80.0, account.getBalance());
    }
}