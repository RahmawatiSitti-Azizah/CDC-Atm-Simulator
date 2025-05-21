package com.mitrais.cdc.model;

import com.mitrais.cdc.view.*;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class WithdrawSummaryScreenTest extends TestCase {

    private WithdrawSummaryScreen initiateWithdrawSummaryScreen(WelcomeScreen welcomeScreen, String menuInput) {
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream(("1\n" + menuInput).getBytes());
        System.setIn(userInput);
        WithdrawScreen withdrawScreen = new WithdrawScreen(welcomeScreen, new Scanner(System.in));
        return (WithdrawSummaryScreen) withdrawScreen.display();
    }

    public void testDisplayWith1Input() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        WithdrawSummaryScreen withdrawSummaryScreen = initiateWithdrawSummaryScreen(welcomeScreen, "1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        assertEquals(90.0, welcomeScreen.getLoginListAccount().get(0).getBalance());
        assertTrue(nextScreen instanceof TransactionScreen);
    }

    public void testDisplayWith2Input() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        WithdrawSummaryScreen withdrawSummaryScreen = initiateWithdrawSummaryScreen(welcomeScreen, "2\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        assertEquals(90.0, welcomeScreen.getLoginListAccount().get(0).getBalance());
        assertTrue(nextScreen instanceof WelcomeScreen);
    }

    public void testDisplayWithOtherInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        WithdrawSummaryScreen withdrawSummaryScreen = initiateWithdrawSummaryScreen(welcomeScreen, "as1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        assertEquals(90.0, welcomeScreen.getLoginListAccount().get(0).getBalance());
        assertTrue(nextScreen instanceof WelcomeScreen);
    }

    public void testDisplayWithNonNumericInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        WithdrawSummaryScreen withdrawSummaryScreen = initiateWithdrawSummaryScreen(welcomeScreen, "jj\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        assertEquals(90.0, welcomeScreen.getLoginListAccount().get(0).getBalance());
        assertTrue(nextScreen instanceof WelcomeScreen);
    }
}