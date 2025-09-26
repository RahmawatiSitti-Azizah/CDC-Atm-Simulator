package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import com.mitrais.cdc.service.impl.ServiceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HistoryTransactionScreenTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    public static void setUp() {
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(100), "TEST01", "112277", "012108"));
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(100), "TEST02", "112288", "012109"));
    }

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    public HistoryTransactionScreen setupHistoryTransactionScreenWithInput(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        Account userAccount = RepositoryFactory.createAccountRepository().getAccountByAccountNumber("112277");
        return HistoryTransactionScreen.getInstance(userAccount, new java.util.Scanner(System.in), ServiceFactory.createTransactionService());
    }

    @Test
    public void testDisplayWithTransactionHistoryThenShouldPrintTransactionAndGoToTransactionScreen() {
        HistoryTransactionScreen historyTransactionScreen = setupHistoryTransactionScreenWithInput("");
        RepositoryFactory.createTransactionRepository().saveTransaction(new Transaction(new Account(null, null, "112277", null),
                null, new Dollar(10), null, "Withdraw", null));
        setUpSystemOutCapturer();
        Assertions.assertTrue(historyTransactionScreen.display() instanceof TransactionScreen);
        String outputString = outputStreamCaptor.toString();
        Assertions.assertTrue(outputString.contains("Withdraw") || outputString.contains("Transfer"));
        closeSystemOutCapturer();
    }

    @Test
    public void testDisplayWithNoTransactionHistoryThenShouldPrintNoTransactionMessageAndGoToTransactionScreen() {
        Account userAccount = new Account(new Dollar(100), "Test User", "112288", "012108");
        HistoryTransactionScreen historyTransactionScreen = HistoryTransactionScreen.getInstance(userAccount, new java.util.Scanner(System.in), ServiceFactory.createTransactionService());
        setUpSystemOutCapturer();
        Assertions.assertTrue(historyTransactionScreen.display() instanceof TransactionScreen);
        String outputString = outputStreamCaptor.toString();
        Assertions.assertTrue(outputString.contains("No transaction history found for account: 112288"));
        closeSystemOutCapturer();
    }
}