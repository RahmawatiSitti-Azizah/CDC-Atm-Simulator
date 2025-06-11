package com.mitrais.cdc.model;

public abstract class Money {
    protected long amount;
    protected String currency;

    public Money(long amount) {
        this.amount = amount;
    }

    public boolean isMultipleOf(int multipleFactor) {
        return amount % multipleFactor == 0;
    }

    public abstract void subtract(Money currency);

    public abstract boolean isMoreThan(Money currency);

    public abstract boolean isMoreThanOrEquals(Money currency);

    public abstract boolean isAmountEqual(Money currency);

    public abstract void add(Money currency);

    @Override
    public String toString() {
        return currency + amount;
    }
}
