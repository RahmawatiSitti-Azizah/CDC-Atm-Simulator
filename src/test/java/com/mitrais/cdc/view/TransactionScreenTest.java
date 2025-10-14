package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TransactionScreenTest {

    private static final double BALANCE_AMOUNT = 100.0;

    @BeforeAll
    public static void setUp() {
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(BALANCE_AMOUNT), "TEST01", "113311", "012108"));
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(BALANCE_AMOUNT), "TEST02", "113322", "012109"));
    }

    @Test
    public void testDisplayMenu1() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("113311\n012108\n1\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof WithdrawScreen);
    }

    @Test
    public void testDisplayMenu2() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("113311\n012108\n2\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof FundTransferScreen);
    }

    @Test
    public void testDisplayMenu3() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("113311\n012108\n3\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof HistoryTransactionScreen);
    }

    @Test
    public void testDisplayMenu4() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("113311\n012108\n4\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayInvalidMenuInput() {
        WelcomeScreen welcomScreenTest = WelcomeScreenTest.getWelcomeScreenTest("113311\n012108\nhgjhg\n");
        Screen transactionScreen = welcomScreenTest.display();
        Assertions.assertTrue(transactionScreen.display() instanceof WelcomeScreen);
    }
}