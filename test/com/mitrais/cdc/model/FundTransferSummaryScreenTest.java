package com.mitrais.cdc.model;

import com.mitrais.cdc.view.*;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class FundTransferSummaryScreenTest extends TestCase {

    public void testDisplayWith1Input() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream(("112244\n10\n550554\n1\n1\n").getBytes());
        System.setIn(userInput);
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in), 1);
        FundTransferSummaryScreen fundTransferSummaryScreen = (FundTransferSummaryScreen) fundTransferScreen.display();
        Screen nextScreen = fundTransferSummaryScreen.display();
        assertTrue(nextScreen instanceof TransactionScreen);
    }

    public void testDisplayWith2Input() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream(("112244\n10\n550554\n1\n2\n").getBytes());
        System.setIn(userInput);
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in), 1);
        FundTransferSummaryScreen fundTransferSummaryScreen = (FundTransferSummaryScreen) fundTransferScreen.display();
        Screen nextScreen = fundTransferSummaryScreen.display();
        assertTrue(nextScreen instanceof WelcomeScreen);
    }

    public void testDisplayWithOtherInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream(("112244\n10\n550554\n1\njahs\n").getBytes());
        System.setIn(userInput);
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in), 1);
        FundTransferSummaryScreen fundTransferSummaryScreen = (FundTransferSummaryScreen) fundTransferScreen.display();
        Screen nextScreen = fundTransferSummaryScreen.display();
        assertTrue(nextScreen instanceof WelcomeScreen);
    }
}