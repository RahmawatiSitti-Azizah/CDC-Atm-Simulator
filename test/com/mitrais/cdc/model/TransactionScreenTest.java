package com.mitrais.cdc.model;

import com.mitrais.cdc.view.FundTransferScreen;
import com.mitrais.cdc.view.TransactionScreen;
import com.mitrais.cdc.view.WelcomeScreen;
import com.mitrais.cdc.view.WithdrawScreen;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class TransactionScreenTest extends TestCase {

    public void testDisplayMenu1() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomScreenTest.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("1".getBytes());
        System.setIn(userInput);
        TransactionScreen transactionScreen = new TransactionScreen(welcomScreenTest, new Scanner(System.in));
        assertTrue(transactionScreen.display() instanceof WithdrawScreen);
    }

    public void testDisplayMenu2() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomScreenTest.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("2".getBytes());
        System.setIn(userInput);
        TransactionScreen transactionScreen = new TransactionScreen(welcomScreenTest, new Scanner(System.in));
        assertTrue(transactionScreen.display() instanceof FundTransferScreen);
    }

    public void testDisplayMenu3() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomScreenTest.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("3".getBytes());
        System.setIn(userInput);
        TransactionScreen transactionScreen = new TransactionScreen(welcomScreenTest, new Scanner(System.in));
        assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayInvalidMenuInput() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomScreenTest.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("hgjhg".getBytes());
        System.setIn(userInput);
        TransactionScreen transactionScreen = new TransactionScreen(welcomScreenTest, new Scanner(System.in));
        assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }
}