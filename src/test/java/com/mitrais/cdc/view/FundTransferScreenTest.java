package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repo.impl.RepositoryFactory;
import com.mitrais.cdc.service.impl.ServiceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class FundTransferScreenTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    @BeforeAll
    public static void setUp() {
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(100), "TEST01", "112233", "012108"));
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(100), "TEST02", "112244", "012109"));
    }

    private static Account createAccount(long balance) {
        Account account = new Account(new Dollar(balance), "Jane Doe", "112255", "112233");
        return account;
    }

    private FundTransferScreen getFundTransferScreen(Account sourceAccount, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        return FundTransferScreen.getInstance(sourceAccount, new Scanner(System.in), ServiceFactory.createAccountTransactionService(), ServiceFactory.createAccountValidatorService(), ServiceFactory.createUserInputService(), ServiceFactory.createTransactionAmountValidatorService(), ServiceFactory.createSearchAccountService());
    }

    @Test
    public void testDisplayWithValidInputToFundTransferSummaryScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n50\n\n1\n");
        Screen nextScreen = fundTransferScreen.display();
        Assertions.assertTrue(nextScreen instanceof FundTransferSummaryScreen);
    }

    @Test
    public void testDisplayWithEscForAccountNumberInput() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "Esc\n");
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testWithEmptyAccountNumberGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "\n");
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testWithNonNumericAccountNumberGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "dsda\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid Account"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithIncorrectAccountNumberLengthGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "1231\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Account Number should have 6 digits length"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithUnregisteredAccountNumberGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "123111\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid Account"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithEmptyAmountGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(!outputStreamCaptor.toString().contains("Invalid"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithNonNumericAmountGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\nss\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithLessThanOneAmountGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n0\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Minimum amount to transfer is $1"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithAmountMoreThanAccountBalanceGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n110\n\n1\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Insufficient balance $110"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithMoreThan1000AmountGetErrorMessageAndGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n1001\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Maximum amount to transfer is $1000"));
        closeSystemOutCapturer();
    }

    @Test
    public void testSuccessTransferCorrectSourceAndDestinationAccountBalance() {
        Account account = createAccount(100);
        try {
            Account destinationAccount = ServiceFactory.createSearchAccountService().getByID("112244");
            destinationAccount.increaseBalance(new Dollar(50));
            FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n50\n\n1\n");
            fundTransferScreen.display();
            Assertions.assertTrue(account.getStringBalance().equals("$50"));
            Assertions.assertTrue(destinationAccount.getStringBalance().equals(ServiceFactory.createSearchAccountService().getByID("112244").getStringBalance()));
        } catch (Exception e) {
            Assertions.assertTrue(false);
        }

    }

    @Test
    public void testInputNonEmptyStringOnReferencePromptSceenAngGotoTransactionScreen() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n50\nss\n");
        Screen nextScreen = fundTransferScreen.display();
        Assertions.assertTrue(nextScreen instanceof TransactionScreen);
    }

    @Test
    public void testCancelTrxAtConfirmationScreenGoToTransaction() {
        Account account = createAccount(100);
        FundTransferScreen fundTransferScreen = getFundTransferScreen(account, "112244\n10\n\n2\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        closeSystemOutCapturer();
    }
}