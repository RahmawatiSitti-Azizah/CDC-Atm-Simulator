package com.mitrais.cdc.model;

public class Account implements Loginable {
    private long balance;
    private String name;
    private String accountNumber;
    private String pin;

    public Account(long aBalance, String aName, String anAccountNumber, String aPin) {
        balance = aBalance;
        name = aName;
        accountNumber = anAccountNumber;
        pin = aPin;
    }

    @Override
    public boolean login(String aUsername, String aPassword) {
        if (accountNumber.equals(aUsername)) {
            if (pin.equals(aPassword)) {
                return true;
            }
        }
        return false;
    }

    public void increaseBalance(long amount) throws Exception {
        if (amount < 0) {
            throw new Exception("Invalid amount");
        } else {
            balance += amount;
        }
    }

    public void decreaseBalance(long amount) throws Exception {
        if (amount > balance) {
            throw new Exception("Insufficient balance $" + amount);
        }
        if (amount < 0) {
            throw new Exception("Invalid amount");
        }
        balance -= amount;

    }

    public long getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

}
