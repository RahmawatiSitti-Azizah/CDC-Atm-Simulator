package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import com.mitrais.cdc.service.impl.ServiceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class WithdrawSummaryScreenTest {
    private static final double BALANCE_AMOUNT = 100.0;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    public static void setUp() {
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(BALANCE_AMOUNT), "TEST01", "113377", "012108"));
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(BALANCE_AMOUNT), "TEST02", "113388", "012109"));
    }

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    private static Account createAccount() {
        return RepositoryFactory.createAccountRepository().getAccountByAccountNumber("113377");
    }

    private WithdrawSummaryScreen getWithdrawSummaryScreen(Account sourceAccount, double amount, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        return WithdrawSummaryScreen.getInstance(new Dollar(amount), sourceAccount, new Scanner(System.in), ServiceFactory.createUserInputService(), ServiceFactory.createAccountTransactionService(), ServiceFactory.createTransactionAmountValidatorService());
    }

    @Test
    public void testDisplayWith1InputtoTransactionScreen() {
        Account account = createAccount();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assertions.assertTrue(nextScreen instanceof TransactionScreen);
    }

    @Test
    public void testDisplayWith2InputtoWelcomeScreen() {
        Account account = createAccount();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "2\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assertions.assertTrue(nextScreen instanceof WelcomeScreen);
    }

    @Test
    public void testOtherInputToWelcomeScreen() {
        Account account = createAccount();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "as1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assertions.assertTrue(nextScreen instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithNonNumericInput() {
        Account account = createAccount();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "jj\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assertions.assertTrue(nextScreen instanceof WelcomeScreen);
    }

    @Test
    public void testWithdrawWithValidAmount() throws Exception {
        Account account = createAccount();
        Account initialAccount = RepositoryFactory.createAccountRepository().getAccountByAccountNumber("113377");
        initialAccount.decreaseBalance(new Dollar(20.0));
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 20, "jj\n");
        withdrawSummaryScreen.display();
        Assertions.assertTrue(account.getStringBalance().equals(initialAccount.getStringBalance()));
    }

    @Test
    public void testWithdrawWithNonMultipleOf10Amount() {
        Account account = createAccount();
        String initialBalance = account.getStringBalance();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 5, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        Assertions.assertTrue(account.getStringBalance().equals(initialBalance));
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithdrawWithExceedMaximumAmount() {
        Account account = createAccount();
        String initialBalance = account.getStringBalance();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 1010, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        Assertions.assertTrue(account.getStringBalance().equals(initialBalance));
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Maximum amount to transfer is $1000"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithdrawWithAmountExceedAccountBalance() {
        Account account = createAccount();
        String initialBalance = account.getStringBalance();
        WithdrawSummaryScreen withdrawSummaryScreen = getWithdrawSummaryScreen(account, 500, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        Assertions.assertTrue(account.getStringBalance().equals(initialBalance));
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Insufficient balance $500"));
        closeSystemOutCapturer();
    }
}