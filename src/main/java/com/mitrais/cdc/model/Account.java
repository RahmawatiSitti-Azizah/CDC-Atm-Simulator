package com.mitrais.cdc.model;

import com.mitrais.cdc.model.converter.MoneyConverter;
import com.mitrais.cdc.util.ErrorConstant;
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
    private String accountHolderName;
    private String pin;
    @Convert(converter = MoneyConverter.class)
    private Money balance;

    public Account() {
    }

    public Account(String accountNumber, String accountHolderName, String pin, Money balance) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
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
            throw new Exception(ErrorConstant.getInsufficientBalanceErrorMessage(withdrawCurrency));
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

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getPin() {
        return pin;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + accountHolderName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
