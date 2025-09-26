package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class WithdrawScreenTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    public static void setUp() {
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(100), "TEST01", "113355", "012108"));
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(100), "TEST02", "113366", "012109"));
    }

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    public WithdrawScreen getWithdrawScreen(String input) {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("113355\n012108\n1\n" + input);
        Screen transactionScreen = welcomScreenTest.display();
        return (WithdrawScreen) transactionScreen.display();
    }

    @Test
    public void testDisplayWithdrawMenu1toWithdrawSummaryScreen() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("1\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testDisplayWithdrawMenu2toWithdrawSummaryScreen() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("2\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testDisplayWithdrawMenu3toWithdrawSummaryScreen() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("3\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testDisplayWithdrawMenutoOtherAmountScreen() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("4\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof OtherWithdrawnScreen);
    }

    @Test
    public void testDisplayWithdrawMenuExit() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("5\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testDisplayWithdrawInvalidMenu() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("s\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof TransactionScreen);
    }
}