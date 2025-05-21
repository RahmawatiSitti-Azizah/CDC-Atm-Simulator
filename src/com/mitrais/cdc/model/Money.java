package com.mitrais.cdc.model;

import javax.xml.bind.ValidationException;

public abstract class Money {

    /**
     * Get string reflecting the amount of money based on implementation class.
     *
     * @return string reflect the amount of money (not null)
     */
    protected abstract String getAmount();

    /**
     * @return
     */
    public abstract String getCurrency();

    /**
     * @param money
     * @throws ValidationException
     */
    public abstract void addMoney(Money money) throws ValidationException;

    /**
     * @param money
     * @throws ValidationException
     */
    public abstract void deductMoney(Money money) throws ValidationException;

    @Override
    public String toString() {
        return getCurrency() + getAmount();
    }
}
