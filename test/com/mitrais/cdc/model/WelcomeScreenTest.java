package com.mitrais.cdc.model;

import com.mitrais.cdc.view.TransactionScreen;
import com.mitrais.cdc.view.WelcomeScreen;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class WelcomeScreenTest extends TestCase {
    public static WelcomeScreen getWelcomScreenTest(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        return new WelcomeScreen(new Scanner(System.in));
    }

    public void testDisplayReturnTransactionScreen() {
        WelcomeScreen welcomeScreen = getWelcomScreenTest("112233\n012108");
        assertTrue(welcomeScreen.display() instanceof TransactionScreen);
    }

    public void testDisplayWithCorrectAccountButIncorrectPin() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\n012109".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithAccountLengthLessThan6() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("11223\n012109".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithAccountLengthMoreThan6() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("1122334\n012109".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithAccountNotNumber() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("tfghhj\n012109".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithPinNotNumber() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\nfassaa".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithPinLengthLessThan6() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\n01210".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithPinLengthMoreThan6() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\n0121088".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }
}