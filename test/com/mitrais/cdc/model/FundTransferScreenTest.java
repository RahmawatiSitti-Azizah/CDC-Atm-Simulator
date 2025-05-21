package com.mitrais.cdc.model;

import com.mitrais.cdc.view.*;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class FundTransferScreenTest extends TestCase {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    public void testDisplayWithEscForAccountNumberInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("Esc\n".getBytes());
        System.setIn(userInput);
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(nextScreen instanceof TransactionScreen);
    }

    public void testDisplayWithEmptyStringForAccountNumberInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("\n".getBytes());
        System.setIn(userInput);
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(nextScreen instanceof TransactionScreen);
    }

    public void testDisplayWithNonNumericAccountNumber() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("Sasa\n\n".getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(outputStreamCaptor.toString().contains("Invalid account"));
        closeSystemOutCapturer();
    }

    public void testDisplayIncorrectNumericLengthForAccountNumberInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("1231\n\n".getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(outputStreamCaptor.toString().contains("Invalid account"));
        closeSystemOutCapturer();
    }

    public void testDisplayWithAccountNumberNotFound() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("123111\n\n".getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(outputStreamCaptor.toString().contains("Invalid account"));
        closeSystemOutCapturer();
    }

    public void testDisplayWithEmptyAmountInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("112244\n\n".getBytes());
        System.setIn(userInput);
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(nextScreen instanceof TransactionScreen);
    }

    public void testDisplayWithNonNumericAmountInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("112244\nss\n\n".getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    public void testDisplayWithLessThanOneAmountInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("112244\n0\n\n".getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(outputStreamCaptor.toString().contains("Minimum amount to transfer is $1"));
        assertTrue(nextScreen instanceof TransactionScreen);
        closeSystemOutCapturer();
    }

    public void testDisplayWithMoreThanBalanceAmountInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        int amount = 110;
        ByteArrayInputStream userInput = new ByteArrayInputStream(("112244\n" + amount + "\n\n").getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(outputStreamCaptor.toString().contains("Insufficient balance $" + amount));
        assertTrue(nextScreen instanceof TransactionScreen);
        closeSystemOutCapturer();
    }

    public void testDisplayWithMoreThan1000AmountInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("112244\n1001\n\n".getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(outputStreamCaptor.toString().contains("Maximum amount to transfer is $1000"));
        assertTrue(nextScreen instanceof TransactionScreen);
        closeSystemOutCapturer();
    }

    public void testDisplayWithNonNumericReferenceNumberInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("112244\n10\nsa2123\n\n".getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(outputStreamCaptor.toString().contains("Invalid Reference Number"));
        assertTrue(nextScreen instanceof TransactionScreen);
        closeSystemOutCapturer();
    }

    public void testDisplayWithIncorrectReferenceNumberInput() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream("112244\n10\n123123\n\n".getBytes());
        System.setIn(userInput);
        setUpSystemOutCapturer();
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in));
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(outputStreamCaptor.toString().contains("Invalid Reference Number"));
        assertTrue(nextScreen instanceof TransactionScreen);
        closeSystemOutCapturer();
    }

    public void testDisplayToTransferSummaryScreen() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream(("112244\n10\n550554\n1\n").getBytes());
        System.setIn(userInput);
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in), 1);
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(nextScreen instanceof FundTransferSummaryScreen);
        assertEquals(40.0, welcomeScreen.getLoginListAccount().get(1).getBalance());
        assertEquals(90.0, welcomeScreen.getLoginListAccount().get(0).getBalance());
    }

    public void testDisplayToCancelTrxAtConfirmScreen() {
        WelcomeScreen welcomeScreen = WelcomeScreenTest.getWelcomScreenTest("112233\n012108");
        welcomeScreen.display();
        ByteArrayInputStream userInput = new ByteArrayInputStream(("112244\n10\n550554\n2\n").getBytes());
        System.setIn(userInput);
        FundTransferScreen fundTransferScreen = new FundTransferScreen(welcomeScreen, new Scanner(System.in), 1);
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(nextScreen instanceof TransactionScreen);
    }
}