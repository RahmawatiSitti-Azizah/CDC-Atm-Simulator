package com.mitrais.cdc.view;

import junit.framework.TestCase;

public class TransactionScreenTest extends TestCase {

    public void testDisplayMenu1() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomScreenTest("112233\n012108\n1\n");
        Screen transactionScreen = welcomScreenTest.display();
        assertTrue(transactionScreen.display() instanceof WithdrawScreen);
    }

    public void testDisplayMenu2() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomScreenTest("112233\n012108\n2\n");
        Screen transactionScreen = welcomScreenTest.display();
        assertTrue(transactionScreen.display() instanceof FundTransferScreen);
    }

    public void testDisplayMenu3() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomScreenTest("112233\n012108\n3\n");
        Screen transactionScreen = welcomScreenTest.display();
        assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayInvalidMenuInput() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomScreenTest("112233\n012108\nhgjhg\n");
        Screen transactionScreen = welcomScreenTest.display();
        assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }
}