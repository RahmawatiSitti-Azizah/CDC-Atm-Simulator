package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.UserInputServiceImplTest;
import com.mitrais.cdc.util.SessionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class OtherWithdrawnScreenTest {
    private static final double BALANCE_AMOUNT = 100.0;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private OtherWithdrawnScreen otherWithdrawnScreen;
    private SessionContext sessionContext = spy(SessionContext.class);
    private WithdrawSummaryScreen withdrawSummaryScreen = mock(WithdrawSummaryScreen.class);
    private UserInputService userInputService = UserInputServiceImplTest.getUserInputService();

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    private static AccountDto createAccount(double balance) {
        AccountDto account = new AccountDto("112233", "Jane Doe", new Dollar(balance));
        return account;
    }

    private void setUserInput(String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        otherWithdrawnScreen = new OtherWithdrawnScreen(withdrawSummaryScreen, userInputService, new Scanner(System.in), sessionContext);
        otherWithdrawnScreen.setPreviousScreen(mock(WithdrawScreen.class));
    }

    @Test
    public void testValidWithdrawAmountToWithdrawSummaryScreen() {
        AccountDto account = createAccount(BALANCE_AMOUNT);
        sessionContext.setSession(account);
        setUserInput("20\n");
        Assertions.assertTrue(otherWithdrawnScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testWithdrawWithNonNumericAmountToWithdrawScreenAndGetInvalidAmountErrorMessage() {
        AccountDto account = createAccount(BALANCE_AMOUNT);
        sessionContext.setSession(account);
        setUserInput("10s\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(otherWithdrawnScreen.display() instanceof WithdrawScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        Assertions.assertTrue(account.getBalance().toString().equals("$100.0"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithdrawWithMinusAmountToWithdrawScreenAndGetInvalidAmountErrorMessage() {
        AccountDto account = createAccount(BALANCE_AMOUNT);
        sessionContext.setSession(account);
        setUserInput("-10\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(otherWithdrawnScreen.display() instanceof WithdrawScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        Assertions.assertTrue(account.getBalance().toString().equals("$100.0"));
        closeSystemOutCapturer();
    }
}