package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class FundTransferSummaryScreenTest extends TestCase {
    private static Account createAccount(String accountNumber, long balance) {
        Account account = new Account(balance, "Jane Doe", accountNumber, "112233");
        return account;
    }

    private FundTransferSummaryScreen getFundTransferScreen(Account sourceAccount, Account destinationAccount, long amount, String referenceNumber, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        return new FundTransferSummaryScreen(amount, sourceAccount, new Scanner(System.in), destinationAccount, referenceNumber);
    }

    public void testWithMenu1GoToTransactionScreen() {
        Account sourceAccount = createAccount("112233", 200);
        Account destinationAccount = createAccount("112244", 50);
        FundTransferSummaryScreen fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "1\n");
        assertTrue(fundTransferSummaryScreen.display() instanceof TransactionScreen);
    }

    public void testWithMenu2GotoWelcomeScreen() {
        Account sourceAccount = createAccount("112233", 200);
        Account destinationAccount = createAccount("112244", 50);
        FundTransferSummaryScreen fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "2\n");
        assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
    }

    public void testWithOtherInputGoToWelcomeScreen() {
        Account sourceAccount = createAccount("112233", 200);
        Account destinationAccount = createAccount("112244", 50);
        FundTransferSummaryScreen fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "3\n");
        assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
        fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "sqw11\n");
        assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
        fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "\n");
        assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
    }
}