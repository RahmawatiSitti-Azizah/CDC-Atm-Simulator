package com.mitrais.cdc.view;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class WelcomeScreenTest {
    public static WelcomeScreen getWelcomeScreenTest(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        return WelcomeScreen.getInstance(null, new Scanner(System.in));
    }

    @Test
    public void testDisplayReturnTransactionScreen() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\n012108");
        Assert.assertTrue(welcomeScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testDisplayWithCorrectAccountButIncorrectPin() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\n012109");
        Assert.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithAccountLengthLessThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("11223\n012109");
        Assert.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithAccountLengthMoreThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("1122334\n012109");
        Assert.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithAccountNotNumber() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("tfghhj\n012109");
        Assert.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithPinNotNumber() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\nfassaa");
        Assert.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithPinLengthLessThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\n01210");
        Assert.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithPinLengthMoreThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("112233\n0121088");
        Assert.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }
}