package com.mitrais.cdc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    public static final String SPACE = " ";
    private final Account sourceAccount;
    private final Account destinationAccount;
    private final Money amount;
    private final String referenceNumber;
    private final String note;
    private final LocalDateTime transactionDate;

    public Transaction(Account sourceAccountNumber, Account destinationAccountNumber, Money amount, String referenceNumber, String note, LocalDateTime transactionDate) {
        this.sourceAccount = sourceAccountNumber;
        this.destinationAccount = destinationAccountNumber;
        this.amount = amount;
        this.referenceNumber = referenceNumber;
        this.note = note;
        this.transactionDate = transactionDate;
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

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    @Override
    public String toString() {
        return transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + SPACE + referenceNumber + SPACE + note + SPACE +
                (destinationAccount != null ?
                        ("to " + destinationAccount.getName() + SPACE) : "") + amount.toString();
    }

    public String printIncomingTransaction() {
        return transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + SPACE + referenceNumber + SPACE + note + SPACE + "from" + SPACE + sourceAccount.getName() + SPACE + amount.toString();
    }
}
