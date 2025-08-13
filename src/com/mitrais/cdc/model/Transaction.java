package com.mitrais.cdc.model;

public class Transaction {
    public static final String SPACE = " ";
    private final Account sourceAccount;
    private final Account destinationAccount;
    private final Money amount;
    private final String referenceNumber;
    private final String note;

    public Transaction(Account sourceAccountNumber, Account destinationAccountNumber, Money amount, String referenceNumber, String note) {
        this.sourceAccount = sourceAccountNumber;
        this.destinationAccount = destinationAccountNumber;
        this.amount = amount;
        this.referenceNumber = referenceNumber;
        this.note = note;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public Money getAmount() {
        return amount;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return referenceNumber + SPACE + note + SPACE +
                (destinationAccount != null ?
                        ("to " + destinationAccount.getName() + SPACE + amount.toString())
                        : amount.toString());
    }
}
