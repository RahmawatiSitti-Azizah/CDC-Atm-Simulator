package com.mitrais.cdc.view;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class WelcomeScreenTest extends TestCase {
    public static WelcomeScreen getWelcomeScreenTest(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        return WelcomeScreen.getInstance(null, new Scanner(System.in));
    }

    public void testDisplayReturnTransactionScreen() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\n012108");
        assertTrue(welcomeScreen.display() instanceof TransactionScreen);
    }

    public void testDisplayWithCorrectAccountButIncorrectPin() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\n012109");
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithAccountLengthLessThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("11223\n012109");
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithAccountLengthMoreThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("1122334\n012109");
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithAccountNotNumber() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("tfghhj\n012109");
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithPinNotNumber() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\nfassaa");
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithPinLengthLessThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\n01210");
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithPinLengthMoreThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\n0121088");
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }
}