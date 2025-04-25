package com.mitrais.cdc.model;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class OtherWithdrawnScreenTest extends TestCase {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    public void testDisplayValidAmount() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("40".getBytes());
        System.setIn(userInput);
        OtherWithdrawnScreen otherWithdrawnScreen = new OtherWithdrawnScreen(welcomeScreen, new Scanner(System.in));
        assertTrue(otherWithdrawnScreen.display() instanceof WithdrawSummaryScreen);
    }

    public void testGetAmountNonNumberInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("40m".getBytes());
        System.setIn(userInput);
        OtherWithdrawnScreen otherWithdrawnScreen = new OtherWithdrawnScreen(welcomeScreen, new Scanner(System.in));
        setUpSystemOutCapturer();
        assertEquals(0.0, otherWithdrawnScreen.getAmount());
        assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    public void testGetAmountNotMultipleOf10() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("35".getBytes());
        System.setIn(userInput);
        OtherWithdrawnScreen otherWithdrawnScreen = new OtherWithdrawnScreen(welcomeScreen, new Scanner(System.in));
        setUpSystemOutCapturer();
        assertEquals(0.0, otherWithdrawnScreen.getAmount());
        assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    public void testGetAmountInsufficientBalance() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("110".getBytes());
        System.setIn(userInput);
        OtherWithdrawnScreen otherWithdrawnScreen = new OtherWithdrawnScreen(welcomeScreen, new Scanner(System.in));
        setUpSystemOutCapturer();
        assertEquals(0.0, otherWithdrawnScreen.getAmount());
        assertTrue(outputStreamCaptor.toString().contains("Insufficient balance $110"));
        closeSystemOutCapturer();
    }

    public void testGetAmountExceedMaximumAmount() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("1010".getBytes());
        System.setIn(userInput);
        OtherWithdrawnScreen otherWithdrawnScreen = new OtherWithdrawnScreen(welcomeScreen, new Scanner(System.in));
        setUpSystemOutCapturer();
        assertEquals(0.0, otherWithdrawnScreen.getAmount());
        assertTrue(outputStreamCaptor.toString().contains("Maximum amount to transfer is $1000"));
        closeSystemOutCapturer();
    }
}