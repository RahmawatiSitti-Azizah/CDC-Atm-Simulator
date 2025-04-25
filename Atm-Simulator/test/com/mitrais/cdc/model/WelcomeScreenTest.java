package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WelcomeScreenTest extends TestCase {
    public static WelcomeScreen getWelcomScreenTest(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        return new WelcomeScreen(Main.getListAccount(), new Scanner(System.in));
    }

    public void testDisplayReturnTransactionScreen() {
        WelcomeScreen welcomeScreen = getWelcomScreenTest("112233\n012108");
        assertTrue(welcomeScreen.display() instanceof TransactionScreen);
    }

    public void testDisplayWithCorrectAccountButIncorrectPin() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\n012109".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(Main.getListAccount(), new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithAccountLengthLessThan6() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("11223\n012109".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(Main.getListAccount(), new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithAccountLengthMoreThan6() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("1122334\n012109".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(Main.getListAccount(), new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithAccountNotNumber() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("tfghhj\n012109".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(Main.getListAccount(), new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithPinNotNumber() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\nfassaa".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(Main.getListAccount(), new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithPinLengthLessThan6() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\n01210".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(Main.getListAccount(), new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testDisplayWithPinLengthMoreThan6() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\n0121088".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(Main.getListAccount(), new Scanner(System.in));
        assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    public void testValidateAccountNull() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(new ArrayList<>(), new Scanner(System.in));
        assertFalse(welcomeScreen.validateUser(null));
    }

    public void testValidatePinNull() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(new ArrayList<>(), new Scanner(System.in));
        assertFalse(welcomeScreen.validatePassword(null));
    }

    public void testGetLoginAccount() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\n012108".getBytes());
        System.setIn(userInput);
        WelcomeScreen welcomeScreen = new WelcomeScreen(Main.getListAccount(), new Scanner(System.in));
        assertNull(welcomeScreen.getLoginAccount());
        welcomeScreen.display();
        assertNotNull(welcomeScreen.getLoginAccount());
    }

    public void testGetLoginListAccount() {
        ByteArrayInputStream userInput = new ByteArrayInputStream("112233\n012108".getBytes());
        System.setIn(userInput);
        List<Account> listAccount = Main.getListAccount();
        WelcomeScreen welcomeScreen = new WelcomeScreen(listAccount, new Scanner(System.in));
        assertEquals(listAccount, welcomeScreen.getLoginListAccount());
    }
}