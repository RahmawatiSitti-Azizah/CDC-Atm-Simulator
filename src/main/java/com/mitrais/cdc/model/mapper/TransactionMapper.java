package com.mitrais.cdc.model.mapper;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.model.dto.TransactionDto;

public class TransactionMapper {
    public static Transaction toEntity(TransactionDto dto) {
        assert dto != null;
        Account sourceAccount = new Account(dto.getSourceAccountNumber(), dto.getDestinationAccountHolderName(), null, null);
        Account destinationAccount = dto.getDestinationAccountNumber() != null ?
                new Account(dto.getSourceAccountNumber(), dto.getDestinationAccountHolderName(), null, null) : null;
        return new Transaction(dto.getId(), sourceAccount, destinationAccount, dto.getAmount(), dto.getReferenceNumber(), dto.getNote(), dto.getTransactionDate());
    }

    public static TransactionDto toTransactionDto(Transaction entity) {
        assert entity != null;
        TransactionDto.Builder builder = new TransactionDto.Builder()
                .id(entity.getId())
                .sourceAccountNumber(entity.getSourceAccount().getAccountNumber())
                .sourceAccountHolderName(entity.getSourceAccount().getAccountHolderName())
                .amount(entity.getAmount())
                .referenceNumber(entity.getReferenceNumber())
                .note(entity.getNote())
                .transactionDate(entity.getTransactionDate());
        return entity.getDestinationAccount() != null ? builder
                .destinationAccountNumber(entity.getDestinationAccount().getAccountNumber())
                .destinationAccountHolderName(entity.getDestinationAccount().getAccountHolderName())
                .build() : builder.build();
    }
}
