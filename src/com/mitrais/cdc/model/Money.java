package com.mitrais.cdc.model;

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
     * @throws Exception
     */
    public abstract void addMoney(Money money) throws Exception;

    /**
     * @param money
     * @throws Exception
     */
    public abstract void deductMoney(Money money) throws Exception;

    @Override
    public String toString() {
        return getCurrency() + getAmount();
    }
}
