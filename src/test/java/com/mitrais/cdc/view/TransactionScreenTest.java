package com.mitrais.cdc.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TransactionScreenTest {
    @Test
    public void testDisplayMenu1() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("112233\n012108\n1\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof WithdrawScreen);
    }

    @Test
    public void testDisplayMenu2() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("112233\n012108\n2\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof FundTransferScreen);
    }

    @Test
    public void testDisplayMenu3() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("112233\n012108\n3\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof HistoryTransactionScreen);
    }

    @Test
    public void testDisplayMenu4() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("112233\n012108\n4\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayInvalidMenuInput() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("112233\n012108\nhgjhg\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }
}