package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HistoryTransactionScreenTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    public HistoryTransactionScreen setupHistoryTransactionScreenWithInput(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        Account userAccount = RepositoryFactory.createAccountRepository().getAccountByAccountNumber("112233");
        return HistoryTransactionScreen.getInstance(userAccount, new java.util.Scanner(System.in));
    }

    @Test
    public void testDisplayWithTransactionHistoryThenShouldPrintTransactionAndGoToTransactionScreen() {
        HistoryTransactionScreen historyTransactionScreen = setupHistoryTransactionScreenWithInput("");
        RepositoryFactory.createTransactionRepository().saveTransaction(new Transaction(new Account(null, null, "112233", null),
                null, new Dollar(10), null, "Withdraw", null));
        setUpSystemOutCapturer();
        Assert.assertTrue(historyTransactionScreen.display() instanceof TransactionScreen);
        String outputString = outputStreamCaptor.toString();
        Assert.assertTrue(outputString.contains("Withdraw") || outputString.contains("Transfer"));
        closeSystemOutCapturer();
    }

    @Test
    public void testDisplayWithNoTransactionHistoryThenShouldPrintNoTransactionMessageAndGoToTransactionScreen() {
        Account userAccount = new Account(new Dollar(100), "Test User", "112255", "012108");
        HistoryTransactionScreen historyTransactionScreen = HistoryTransactionScreen.getInstance(userAccount, new java.util.Scanner(System.in));
        setUpSystemOutCapturer();
        Assert.assertTrue(historyTransactionScreen.display() instanceof TransactionScreen);
        String outputString = outputStreamCaptor.toString();
        Assert.assertTrue(outputString.contains("No transaction history found for account: 112255"));
        closeSystemOutCapturer();
    }
}