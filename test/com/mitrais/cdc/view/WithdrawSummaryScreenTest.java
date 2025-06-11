package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class WithdrawSummaryScreenTest extends TestCase {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    private static Account createAccount(long balance) {
        Account account = new Account(new Dollar(balance), "Jane Doe", "112255", "112233");
        return account;
    }

    private WithdrawSummaryScreen getWithdrawSummaryScreen(Account sourceAccount, long amount, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        return new WithdrawSummaryScreen(new Dollar(amount), sourceAccount, new Scanner(System.in));
    }

    public void testDisplayWith1InputtoTransactionScreen() {
        Account account = createAccount(100);
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        assertTrue(nextScreen instanceof TransactionScreen);
    }

    public void testDisplayWith2InputtoWelcomeScreen() {
        Account account = createAccount(100);
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "2\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        assertTrue(nextScreen instanceof WelcomeScreen);
    }

    public void testOtherInputToWelcomeScreen() {
        Account account = createAccount(100);
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "as1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        assertTrue(nextScreen instanceof WelcomeScreen);
    }

    public void testDisplayWithNonNumericInput() {
        Account account = createAccount(100);
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "jj\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        assertTrue(nextScreen instanceof WelcomeScreen);
    }

    public void testWithdrawWithValidAmount() {
        Account account = createAccount(100);
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "jj\n");
        withdrawSummaryScreen.display();
        assertTrue(account.getBalance().isAmountEqual(new Dollar(80)));
    }

    public void testWithdrawWithNonMultipleOf10Amount() {
        Account account = createAccount(100);
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 5, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        assertTrue(account.getBalance().isAmountEqual(new Dollar(100)));
        assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    public void testWithdrawWithExceedMaximumAmount() {
        Account account = createAccount(10000);
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 1010, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        assertTrue(account.getBalance().isAmountEqual(new Dollar(10000)));
        assertTrue(outputStreamCaptor.toString().contains("Maximum amount to transfer is $1000"));
        closeSystemOutCapturer();
    }

    public void testWithdrawWithAmountExceedAccountBalance() {
        Account account = createAccount(70);
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 80, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        assertTrue(account.getBalance().isAmountEqual(new Dollar(70)));
        assertTrue(outputStreamCaptor.toString().contains("Insufficient balance $80"));
        closeSystemOutCapturer();
    }
}