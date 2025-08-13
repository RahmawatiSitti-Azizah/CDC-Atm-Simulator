package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class WithdrawSummaryScreenTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    private static Account createAccount() {
        return RepositoryFactory.createAccountRepository().getAccountByAccountNumber("112233");
    }

    private WithdrawSummaryScreen getWithdrawSummaryScreen(Account sourceAccount, long amount, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        return WithdrawSummaryScreen.getInstance(new Dollar(amount), sourceAccount, new Scanner(System.in));
    }

    @Test
    public void testDisplayWith1InputtoTransactionScreen() {
        Account account = createAccount();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assert.assertTrue(nextScreen instanceof TransactionScreen);
    }

    @Test
    public void testDisplayWith2InputtoWelcomeScreen() {
        Account account = createAccount();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "2\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assert.assertTrue(nextScreen instanceof WelcomeScreen);
    }

    @Test
    public void testOtherInputToWelcomeScreen() {
        Account account = createAccount();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "as1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assert.assertTrue(nextScreen instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithNonNumericInput() {
        Account account = createAccount();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "jj\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assert.assertTrue(nextScreen instanceof WelcomeScreen);
    }

    @Test
    public void testWithdrawWithValidAmount() throws Exception {
        Account account = createAccount();
        Account initialAccount = RepositoryFactory.createAccountRepository().getAccountByAccountNumber("112233");
        initialAccount.decreaseBalance(new Dollar(20));
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "jj\n");
        withdrawSummaryScreen.display();
        Assert.assertTrue(account.getStringBalance().equals(initialAccount.getStringBalance()));
    }

    @Test
    public void testWithdrawWithNonMultipleOf10Amount() {
        Account account = createAccount();
        String initialBalance = account.getStringBalance();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 5, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        Assert.assertTrue(account.getStringBalance().equals(initialBalance));
        Assert.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithdrawWithExceedMaximumAmount() {
        Account account = createAccount();
        String initialBalance = account.getStringBalance();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 1010, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        Assert.assertTrue(account.getStringBalance().equals(initialBalance));
        Assert.assertTrue(outputStreamCaptor.toString().contains("Maximum amount to transfer is $1000"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithdrawWithAmountExceedAccountBalance() {
        Account account = createAccount();
        String initialBalance = account.getStringBalance();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 500, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        Assert.assertTrue(account.getStringBalance().equals(initialBalance));
        Assert.assertTrue(outputStreamCaptor.toString().contains("Insufficient balance $500"));
        closeSystemOutCapturer();
    }
}