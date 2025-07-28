package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class OtherWithdrawnScreenTest extends TestCase {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    private static Account createAccount(long balance) {
        Account account = new Account(new Dollar(balance), "Jane Doe", "112255", "112233");
        return account;
    }

    private OtherWithdrawnScreen getOtherWithdrawScreen(Account sourceAccount, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        return OtherWithdrawnScreen.getInstance(sourceAccount, new Scanner(System.in));
    }

    public void testValidWithdrawAmountToWithdrawSummaryScreen() {
        Account account = createAccount(100);
        OtherWithdrawnScreen otherWithdrawnScreen = getOtherWithdrawScreen(account, "20\n");
        assertTrue(otherWithdrawnScreen.display() instanceof WithdrawSummaryScreen);
    }

    public void testWithdrawWithNonNumericAmountToWithdrawScreenAndGetInvalidAmountErrorMessage() {
        Account account = createAccount(100);
        OtherWithdrawnScreen otherWithdrawnScreen = getOtherWithdrawScreen(account, "10s\n");
        setUpSystemOutCapturer();
        assertTrue(otherWithdrawnScreen.display() instanceof WithdrawScreen);
        assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        assertTrue(account.getStringBalance().equals("$100"));
        closeSystemOutCapturer();
    }

    public void testWithdrawWithMinusAmountToWithdrawScreenAndGetInvalidAmountErrorMessage() {
        Account account = createAccount(100);
        OtherWithdrawnScreen otherWithdrawnScreen = getOtherWithdrawScreen(account, "-10\n");
        setUpSystemOutCapturer();
        assertTrue(otherWithdrawnScreen.display() instanceof WithdrawScreen);
        assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        assertTrue(account.getStringBalance().equals("$100"));
        closeSystemOutCapturer();
    }
}