package com.mitrais.cdc.model;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class TransactionScreenTest extends TestCase {

    public void testDisplayMenu1() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("1".getBytes());
        System.setIn(userInput);
        TransactionScreen transactionScreen = new TransactionScreen(WelcomeScreenTest.getWelcomScreenTest(), new Scanner(System.in));
        assertTrue(transactionScreen.display() instanceof WithdrawScreen);
    }

    public void testDisplayMenu2() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("2".getBytes());
        System.setIn(userInput);
        TransactionScreen transactionScreen = new TransactionScreen(WelcomeScreenTest.getWelcomScreenTest(), new Scanner(System.in));
        assertTrue(transactionScreen.display() instanceof FundTransferScreen);
    }

    public void testDisplayMenu3() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("3".getBytes());
        System.setIn(userInput);
        TransactionScreen transactionScreen = new TransactionScreen(WelcomeScreenTest.getWelcomScreenTest(), new Scanner(System.in));
        assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayInvalidMenuInput() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("hgjhg".getBytes());
        System.setIn(userInput);
        TransactionScreen transactionScreen = new TransactionScreen(WelcomeScreenTest.getWelcomScreenTest(), new Scanner(System.in));
        assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }
}