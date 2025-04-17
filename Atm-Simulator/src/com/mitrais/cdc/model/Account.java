package com.mitrais.cdc.model;

public class Account implements Loginable {
    private Double balance;
    private String name;
    private String accountNumber;
    private String pin;

    public Account(Double aBalance, String aName, String anAccountNumber, String aPin) {
        balance = aBalance;
        name = aName;
        accountNumber = anAccountNumber;
        pin = aPin;
    }

    @Override
    public Boolean login(String aUsername, String aPassword) {
        if (accountNumber.equals(aUsername)) {
            if (pin.equals(aPassword)) {
                return true;
            }
        }
        return false;
    }

    public Double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
