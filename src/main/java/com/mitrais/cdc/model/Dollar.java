package com.mitrais.cdc.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Dollar extends Money {

    public Dollar(Double amount) {
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

    @Override
    public void insertToPreparedStatement(PreparedStatement ps, int index) throws SQLException {
        ps.setDouble(index, amount);
    }
}
