package com.mitrais.cdc.model;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class WithdrawScreenTest extends TestCase {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    public void testDisplayWithdrawMenu1() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("1".getBytes());
        System.setIn(userInput);
        WithdrawScreen withdrawScreen = new WithdrawScreen(welcomeScreen, new Scanner(System.in));
        Screen withdrawSummaryScreen = withdrawScreen.display();
        assertTrue(withdrawSummaryScreen instanceof WithdrawSummaryScreen);
    }

    public void testDisplayWithdrawMenu2() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("2".getBytes());
        System.setIn(userInput);
        WithdrawScreen withdrawScreen = new WithdrawScreen(welcomeScreen, new Scanner(System.in));
        Screen withdrawSummaryScreen = withdrawScreen.display();
        assertTrue(withdrawSummaryScreen instanceof WithdrawSummaryScreen);
    }

    public void testDisplayWithdrawMenu3() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("3".getBytes());
        System.setIn(userInput);
        WithdrawScreen withdrawScreen = new WithdrawScreen(welcomeScreen, new Scanner(System.in));
        Screen withdrawSummaryScreen = withdrawScreen.display();
        assertTrue(withdrawSummaryScreen instanceof WithdrawSummaryScreen);
    }

    public void testDisplayWithdrawMenuOtherAmount() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("4".getBytes());
        System.setIn(userInput);
        WithdrawScreen withdrawScreen = new WithdrawScreen(welcomeScreen, new Scanner(System.in));
        Screen otherWithdrawnScreen = withdrawScreen.display();
        assertEquals(100.0, welcomeScreen.getLoginAccount().getBalance());
        assertTrue(otherWithdrawnScreen instanceof OtherWithdrawnScreen);
    }

    public void testDisplayWithdrawMenuExit() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("5".getBytes());
        System.setIn(userInput);
        WithdrawScreen withdrawScreen = new WithdrawScreen(welcomeScreen, new Scanner(System.in));
        Screen transactionScreen = withdrawScreen.display();
        assertEquals(100.0, welcomeScreen.getLoginAccount().getBalance());
        assertTrue(transactionScreen instanceof TransactionScreen);
    }

    public void testDisplayWithdrawInvalidMenu() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("s".getBytes());
        System.setIn(userInput);
        WithdrawScreen withdrawScreen = new WithdrawScreen(welcomeScreen, new Scanner(System.in));
        Screen transactionScreen = withdrawScreen.display();
        assertEquals(100.0, welcomeScreen.getLoginAccount().getBalance());
        assertTrue(transactionScreen instanceof TransactionScreen);
    }

    public void testDisplayWithdrawInsufficientBalance() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112244\n932012");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("3\n8".getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        WithdrawScreen withdrawScreen = new WithdrawScreen(welcomeScreen, new Scanner(System.in));
        withdrawScreen.display();
        assertEquals(30.0, welcomeScreen.getLoginAccount().getBalance());
        assertTrue(outputStreamCaptor.toString().contains("Insufficient balance"));
        closeSystemOutCapturer();
    }
}