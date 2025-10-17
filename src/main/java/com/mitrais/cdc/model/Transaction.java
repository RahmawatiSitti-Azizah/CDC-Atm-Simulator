package com.mitrais.cdc.model;

import com.mitrais.cdc.model.converter.MoneyConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Transaction {
    public static final String SPACE = " ";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "source_account", referencedColumnName = "accountNumber", nullable = true)
    private Account sourceAccount;
    @OneToOne
    @JoinColumn(name = "destination_account", referencedColumnName = "accountNumber", nullable = true)
    private Account destinationAccount;
    @Convert(converter = MoneyConverter.class)
    private Money amount;
    private String referenceNumber;
    private String note;
    private LocalDateTime transactionDate;

    public Transaction() {
    }

    public Transaction(Integer id, Account sourceAccountNumber, Account destinationAccountNumber, Money amount, String referenceNumber, String note, LocalDateTime transactionDate) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return printTransactionDate() + SPACE + referenceNumber + SPACE + note + SPACE +
                (destinationAccount != null ?
                        ("to " + destinationAccount.getAccountHolderName() + SPACE) : "") + amount.toString();
    }

    private String printTransactionDate() {
        return (transactionDate != null ? transactionDate : LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String printIncomingTransaction() {
        return printTransactionDate() + SPACE + referenceNumber + SPACE + note + SPACE + "from" + SPACE + sourceAccount.getAccountHolderName() + SPACE + amount.toString();
    }
}
