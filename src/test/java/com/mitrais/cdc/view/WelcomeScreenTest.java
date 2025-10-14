package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import com.mitrais.cdc.service.impl.ServiceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class WelcomeScreenTest {

    private static final double BALANCE_AMOUNT = 100.0;

    @BeforeAll
    public static void setUp() {
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(BALANCE_AMOUNT), "TEST01", "113333", "012108"));
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(BALANCE_AMOUNT), "TEST02", "113344", "012109"));
    }

    public static WelcomeScreen getWelcomeScreenTest(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        return WelcomeScreen.getInstance(null, new Scanner(System.in), ServiceFactory.createSearchAccountService(), ServiceFactory.createAccountValidatorService());
    }

    @Test
    public void testDisplayReturnTransactionScreen() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("113333\n012108");
        Assertions.assertTrue(welcomeScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testDisplayWithCorrectAccountButIncorrectPin() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("113333\n012109");
        Assertions.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithAccountLengthLessThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("11223\n012109");
        Assertions.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithAccountLengthMoreThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("1122334\n012109");
        Assertions.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithAccountNotNumber() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("tfghhj\n012109");
        Assertions.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithPinNotNumber() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("113333\nfassaa");
        Assertions.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithPinLengthLessThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("113333\n01210");
        Assertions.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithPinLengthMoreThan6() {
        WelcomeScreen welcomeScreen = getWelcomeScreenTest("113333\n0121088");
        Assertions.assertTrue(welcomeScreen.display() instanceof WelcomeScreen);
    }
}