package com.mitrais.cdc.model;

import javax.xml.bind.ValidationException;

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

    public void increaseBalance(long amount) throws ValidationException {
        if (amount < 0) {
            throw new ValidationException("Invalid amount");
        } else {
            balance += amount;
        }
    }

    public void decreaseBalance(long amount) throws ValidationException {
        if (amount < 0) {
            throw new ValidationException("Invalid amount");
        } else {
            balance += amount;
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    /*public void setBalance(double balance) {
        this.balance = balance;
    }*/

}
