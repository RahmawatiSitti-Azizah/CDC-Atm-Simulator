package com.mitrais.cdc.model;

import com.mitrais.cdc.service.impl.ServiceFactory;
import com.mitrais.cdc.view.FundTransferScreen;
import com.mitrais.cdc.view.FundTransferSummaryScreen;
import com.mitrais.cdc.view.Screen;
import com.mitrais.cdc.view.TransactionScreen;
import junit.framework.TestCase;

import javax.xml.bind.ValidationException;
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

    private static Account createAccount(long balance) {
        Account account = new Account(balance, "Jane Doe", "112255", "112233");
        return account;
    }

    private FundTransferScreen getFundTransferScreen(Account sourceAccount, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        return new FundTransferScreen(sourceAccount, new Scanner(System.in));
    }

    public void testDisplayWithValidInputToFundTransferSummaryScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n50\n\n1\n");
        Screen nextScreen = fundTransferScreen.display();
        assertTrue(nextScreen instanceof FundTransferSummaryScreen);
    }

    public void testDisplayWithEscForAccountNumberInput() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "Esc\n");
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
    }

    public void testWithEmptyAccountNumberGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "\n");
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
    }

    public void testWithNonNumericAccountNumberGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "dsda\n");
        setUpSystemOutCapturer();
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        assertTrue(outputStreamCaptor.toString().contains("Invalid Account"));
        closeSystemOutCapturer();
    }

    public void testWithIncorrectAccountNumberLengthGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "1231\n");
        setUpSystemOutCapturer();
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        assertTrue(outputStreamCaptor.toString().contains("Account Number should have 6 digits length"));
        closeSystemOutCapturer();
    }

    public void testWithUnregisteredAccountNumberGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "123111\n");
        setUpSystemOutCapturer();
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        assertTrue(outputStreamCaptor.toString().contains("Invalid Account"));
        closeSystemOutCapturer();
    }

    public void testWithEmptyAmountGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n\n");
        setUpSystemOutCapturer();
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        assertTrue(!outputStreamCaptor.toString().contains("Invalid"));
        closeSystemOutCapturer();
    }

    public void testWithNonNumericAmountGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\nss\n");
        setUpSystemOutCapturer();
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    public void testWithLessThanOneAmountGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n0\n");
        setUpSystemOutCapturer();
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        assertTrue(outputStreamCaptor.toString().contains("Minimum amount to transfer is $1"));
        closeSystemOutCapturer();
    }

    public void testWithAmountMoreThanAccountBalanceGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n110\n");
        setUpSystemOutCapturer();
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        assertTrue(outputStreamCaptor.toString().contains("Insufficient balance $110"));
        closeSystemOutCapturer();
    }

    public void testWithMoreThan1000AmountGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n1001\n");
        setUpSystemOutCapturer();
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        assertTrue(outputStreamCaptor.toString().contains("Maximum amount to transfer is $1000"));
        closeSystemOutCapturer();
    }

    public void testSuccessTransferCorrectSourceAndDestinationAccountBalance() {
        Account account = createAccount(100);
        try {
            long destinationAccountBalance = ServiceFactory.createSearchAccountValidationService().getByID("112244").getBalance();
            FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n50\n\n1\n");
            fundTransferScreen.display();
            assertEquals(50, account.getBalance());
            assertEquals(destinationAccountBalance + 50, ServiceFactory.createSearchAccountValidationService().getByID("112244").getBalance());
        } catch (ValidationException e) {
            assertTrue(false);
        }

    }

    public void testCancelTrxAtConfirmationScreenGoToTransaction() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n10\n\n2\n");
        setUpSystemOutCapturer();
        assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        closeSystemOutCapturer();
    }
}