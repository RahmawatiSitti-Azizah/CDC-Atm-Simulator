package com.mitrais.cdc.model;

public class Dollar extends Money {
    private int amount;

    public Dollar(int amount) {
        this.amount = amount;
    }

    @Override
    protected String getAmount() {
        return String.valueOf(amount);
    }

    public String getCurrency() {
        return "$";
    }

    public void addMoney(Money money) {
        assert money instanceof Dollar;
        Dollar dollar = (Dollar) money;
        amount += dollar.getValue();
    }

    public int getValue() {
        return amount;
    }

    public void deductMoney(Money money) {
        assert money instanceof Dollar;
        Dollar dollar = (Dollar) money;
        amount -= dollar.getValue();
    }
}
