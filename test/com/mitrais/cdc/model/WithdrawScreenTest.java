package com.mitrais.cdc.model;

import com.mitrais.cdc.view.*;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class WithdrawScreenTest extends TestCase {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    public WithdrawScreen getWithdrawScreen(String input) {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomScreenTest("112233\n012108\n1\n" + input);
        Screen transactionScreen = welcomScreenTest.display();
        return (WithdrawScreen) transactionScreen.display();
    }

    public void testDisplayWithdrawMenu1toWithdrawSummaryScreen() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("1\n");
        assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    public void testDisplayWithdrawMenu2toWithdrawSummaryScreen() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("2\n");
        assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    public void testDisplayWithdrawMenu3toWithdrawSummaryScreen() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("3\n");
        assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    public void testDisplayWithdrawMenutoOtherAmountScreen() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("4\n");
        assertTrue(withdrawScreen.display() instanceof OtherWithdrawnScreen);
    }

    public void testDisplayWithdrawMenuExit() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("5\n");
        assertTrue(withdrawScreen.display() instanceof TransactionScreen);
    }

    public void testDisplayWithdrawInvalidMenu() {
        WithdrawScreen withdrawScreen = getWithdrawScreen("s\n");
        assertTrue(withdrawScreen.display() instanceof TransactionScreen);
    }
}