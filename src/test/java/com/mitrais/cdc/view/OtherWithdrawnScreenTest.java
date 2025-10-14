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

public class OtherWithdrawnScreenTest {
    private static final double BALANCE_AMOUNT = 100.0;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    public static void setUp() {
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(BALANCE_AMOUNT), "TEST01", "112299", "012108"));
        RepositoryFactory.createAccountRepository().saveAccount(new Account(new Dollar(BALANCE_AMOUNT), "TEST02", "112200", "012109"));
    }

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    private static Account createAccount(double balance) {
        Account account = new Account(new Dollar(balance), "Jane Doe", "112299", "112233");
        return account;
    }

    private OtherWithdrawnScreen getOtherWithdrawScreen(Account sourceAccount, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        return OtherWithdrawnScreen.getInstance(sourceAccount, new Scanner(System.in), ServiceFactory.createUserInputService());
    }

    @Test
    public void testValidWithdrawAmountToWithdrawSummaryScreen() {
        Account account = createAccount(BALANCE_AMOUNT);
        OtherWithdrawnScreen otherWithdrawnScreen = getOtherWithdrawScreen(account, "20\n");
        Assertions.assertTrue(otherWithdrawnScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testWithdrawWithNonNumericAmountToWithdrawScreenAndGetInvalidAmountErrorMessage() {
        Account account = createAccount(BALANCE_AMOUNT);
        OtherWithdrawnScreen otherWithdrawnScreen = getOtherWithdrawScreen(account, "10s\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(otherWithdrawnScreen.display() instanceof WithdrawScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        Assertions.assertTrue(account.getStringBalance().equals("$100"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithdrawWithMinusAmountToWithdrawScreenAndGetInvalidAmountErrorMessage() {
        Account account = createAccount(BALANCE_AMOUNT);
        OtherWithdrawnScreen otherWithdrawnScreen = getOtherWithdrawScreen(account, "-10\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(otherWithdrawnScreen.display() instanceof WithdrawScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        Assertions.assertTrue(account.getStringBalance().equals("$100"));
        closeSystemOutCapturer();
    }
}