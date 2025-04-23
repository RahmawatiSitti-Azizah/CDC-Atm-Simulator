package com.mitrais.cdc.model;

public class Account implements Loginable {
    private double balance;
    private String name;
    private String accountNumber;
    private String pin;

    public Account(double aBalance, String aName, String anAccountNumber, String aPin) {
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

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
