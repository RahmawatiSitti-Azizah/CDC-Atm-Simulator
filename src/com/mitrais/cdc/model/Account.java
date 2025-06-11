package com.mitrais.cdc.model;

public class Account implements Loginable {
    private String name;
    private String accountNumber;
    private String pin;
    private Money balance;

    public Account(Money currency, String aName, String anAccountNumber, String aPin) {
        name = aName;
        accountNumber = anAccountNumber;
        pin = aPin;
        balance = currency;
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

    public void increaseBalance(Money currencyAmount) throws Exception {
        validateAmountMoreThanZero(currencyAmount);
        balance.add(currencyAmount);
    }

    public void decreaseBalance(Money withdrawCurrency) throws Exception {
        if (withdrawCurrency.isMoreThan(balance)) {
            throw new Exception("Insufficient balance " + withdrawCurrency.toString());
        }
        validateAmountMoreThanZero(withdrawCurrency);
        balance.subtract(withdrawCurrency);
    }

    private void validateAmountMoreThanZero(Money amount) throws Exception {
        if ((new Dollar(0)).isMoreThanOrEquals(balance)) {
            throw new Exception("Invalid amount");
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Money getBalance() {
        return balance;
    }
}
