package com.mitrais.cdc.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class TransactionTest {
    private static final double BALANCE_AMOUNT = 200.0;
    private static final Dollar TRANSFER_AMOUNT = new Dollar(10.0);
    private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.now();
    private static final Account SOURCE_ACCOUNT = new Account("111111", "Jane Doe", null, new Dollar(BALANCE_AMOUNT));
    private static final Account DESTINATION_ACCOUNT = new Account("111111", "John Doe", null, new Dollar(250.0));
    private static final String TRANSFER_NOTE = "Transfer";
    private static final String WITHDRAW_NOTE = "withdraw";

    @Test
    public void testPrintIncomingTransaction_thenReturnStringContainFromDestinationAccountHolderWithoutSourceAccount() {
        Transaction transferTransaction = new Transaction(null, SOURCE_ACCOUNT, DESTINATION_ACCOUNT, TRANSFER_AMOUNT, "123sads", TRANSFER_NOTE, TRANSACTION_DATE);
        String result = transferTransaction.printIncomingTransaction();
        Assertions.assertTrue(result.contains(TRANSFER_NOTE));
        Assertions.assertTrue(result.contains("from " + SOURCE_ACCOUNT.getAccountHolderName()));
    }

    @Test
    public void testToString_whenWithdrawTransaction_thenReturnStringContainWithdrawNoteWithoutAnyAccountHolderName() {
        Transaction withdrawTransaction = new Transaction(null, SOURCE_ACCOUNT, null, TRANSFER_AMOUNT, "", WITHDRAW_NOTE, TRANSACTION_DATE);
        LocalDateTime transactionDate = LocalDateTime.now();
        withdrawTransaction.setTransactionDate(transactionDate);
        String result = withdrawTransaction.toString();
        Assertions.assertTrue(result.contains(WITHDRAW_NOTE));
        Assertions.assertFalse(result.contains(SOURCE_ACCOUNT.getAccountHolderName()));
        Assertions.assertFalse(result.contains(DESTINATION_ACCOUNT.getAccountHolderName()));
    }

    @Test
    public void testToString_whenWithdrawTransaction_thenReturnStringContainTransferKeywordToAndDestinationAccountHolder() {
        Transaction transferTransaction = new Transaction(null, SOURCE_ACCOUNT, DESTINATION_ACCOUNT, TRANSFER_AMOUNT, "123sads", TRANSFER_NOTE, TRANSACTION_DATE);
        String result = transferTransaction.toString();
        Assertions.assertTrue(result.contains(TRANSFER_NOTE));
        Assertions.assertTrue(result.contains("to " + DESTINATION_ACCOUNT.getAccountHolderName()));
    }
}