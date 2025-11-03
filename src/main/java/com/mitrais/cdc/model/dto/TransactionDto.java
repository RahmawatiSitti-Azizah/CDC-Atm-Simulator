package com.mitrais.cdc.model.dto;

import com.mitrais.cdc.model.Money;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionDto {
    public static final String SPACE = " ";
    private Integer id;
    private String sourceAccountNumber;
    private String sourceAccountHolderName;
    private String destinationAccountNumber;
    private String destinationAccountHolderName;
    private Money amount;
    private String referenceNumber;
    private String note;
    private LocalDateTime transactionDate;

    private TransactionDto(Builder builder) {
        this.amount = builder.amount;
        this.destinationAccountHolderName = builder.destinationAccountHolderName;
        this.destinationAccountNumber = builder.destinationAccountNumber;
        this.id = builder.id;
        this.note = builder.note;
        this.referenceNumber = builder.referenceNumber;
        this.sourceAccountHolderName = builder.sourceAccountHolderName;
        this.sourceAccountNumber = builder.sourceAccountNumber;
        this.transactionDate = builder.transactionDate;
    }

    public Money getAmount() {
        return amount;
    }

    public String getDestinationAccountHolderName() {
        return destinationAccountHolderName;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public Integer getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getSourceAccountHolderName() {
        return sourceAccountHolderName;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    @Override
    public String toString() {
        return printTransactionDate() + SPACE + referenceNumber + SPACE + note + SPACE +
                (destinationAccountNumber != null ?
                        ("to " + destinationAccountHolderName + SPACE) : "") + amount.toString();
    }

    private String printTransactionDate() {
        return (transactionDate != null ? transactionDate : LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String printIncomingTransaction() {
        return printTransactionDate() + SPACE + referenceNumber + SPACE + note + SPACE + "from" + SPACE + sourceAccountHolderName + SPACE + amount.toString();
    }

    public static class Builder {
        private Integer id;
        private String sourceAccountNumber;
        private String sourceAccountHolderName;
        private String destinationAccountNumber;
        private String destinationAccountHolderName;
        private Money amount;
        private String referenceNumber;
        private String note;
        private LocalDateTime transactionDate;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder sourceAccountNumber(String sourceAccountNumber) {
            this.sourceAccountNumber = sourceAccountNumber;
            return this;
        }

        public Builder sourceAccountHolderName(String sourceAccountHolderName) {
            this.sourceAccountHolderName = sourceAccountHolderName;
            return this;
        }

        public Builder destinationAccountNumber(String destinationAccountNumber) {
            this.destinationAccountNumber = destinationAccountNumber;
            return this;
        }

        public Builder destinationAccountHolderName(String destinationAccountHolderName) {
            this.destinationAccountHolderName = destinationAccountHolderName;
            return this;
        }

        public Builder amount(Money amount) {
            this.amount = amount;
            return this;
        }

        public Builder referenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder transactionDate(LocalDateTime transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public TransactionDto build() {
            return new TransactionDto(this);
        }
    }

}
