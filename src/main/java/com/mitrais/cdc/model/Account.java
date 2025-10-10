package com.mitrais.cdc.model;

import com.mitrais.cdc.model.converter.MoneyConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Account implements Loginable {
    @Id
    @Column(length = 6)
    private String accountNumber;
    private String name;
    private String pin;
    @Convert(converter = MoneyConverter.class)
    private Money balance;

    public Account() {
    }

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
        if ((new Dollar(0.0)).isMoreThanOrEquals(amount)) {
            throw new Exception("Invalid amount");
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getStringBalance() {
        return balance.toString();
    }

    public Money getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getPin() {
        return pin;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;
        return name.equals(account.name) && accountNumber.equals(account.accountNumber) && pin.equals(account.pin) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accountNumber, pin, balance);
    }
}
