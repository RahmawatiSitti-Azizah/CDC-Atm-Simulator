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

public class FundTransferSummaryScreenTest {

    @BeforeAll
    public static void setUp() {
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(100), "TEST01", "112255", "012108"));
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(100), "TEST02", "112266", "012109"));
    }

    private static Account createAccount(String accountNumber, long balance) {
        Account account = new Account(new Dollar(balance), "Jane Doe", accountNumber, "112233");
        return account;
    }

    private FundTransferSummaryScreen getFundTransferScreen(Account sourceAccount, Account destinationAccount, long amount, String referenceNumber, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        return FundTransferSummaryScreen.getInstance(new Dollar(amount), sourceAccount, new Scanner(System.in), destinationAccount, referenceNumber, ServiceFactory.createUserInputService());
    }

    @Test
    public void testWithMenu1GoToTransactionScreen() {
        Account sourceAccount = createAccount("112255", 200);
        Account destinationAccount = createAccount("112266", 50);
        FundTransferSummaryScreen fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "1\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testWithMenu2GotoWelcomeScreen() {
        Account sourceAccount = createAccount("112255", 200);
        Account destinationAccount = createAccount("112266", 50);
        FundTransferSummaryScreen fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "2\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testWithOtherInputGoToWelcomeScreen() {
        Account sourceAccount = createAccount("112255", 200);
        Account destinationAccount = createAccount("112266", 50);
        FundTransferSummaryScreen fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "3\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
        fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "sqw11\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
        fundTransferSummaryScreen = getFundTransferScreen(sourceAccount, destinationAccount, 70, "012311", "\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
    }
}