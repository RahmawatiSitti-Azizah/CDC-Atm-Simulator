package com.mitrais.cdc.model;

public class Dollar extends Money {

    public Dollar(long amount) {
        super(amount);
        currency = "$";
    }

    @Override
    public void subtract(Money currency) {
        amount -= currency.amount;
    }

    @Override
    public boolean isMoreThan(Money currency) {
        return amount > currency.amount;
    }

    @Override
    public boolean isMoreThanOrEquals(Money currency) {
        return amount >= currency.amount;
    }

    @Override
    public boolean isAmountEqual(Money currency) {
        return amount == currency.amount;
    }

    @Override
    public void add(Money currency) {
        amount += currency.amount;
    }

}
