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

import static org.mockito.Mockito.*;

public class OtherWithdrawnScreenTest {
    private static final double BALANCE_AMOUNT = 100.0;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private OtherWithdrawnScreen otherWithdrawnScreen;
    private SessionContext sessionContext = spy(SessionContext.class);
    private ScreenManager screenManager = mock(ScreenManager.class);
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
        otherWithdrawnScreen = new OtherWithdrawnScreen(screenManager, userInputService, new Scanner(System.in), sessionContext);
    }

    @Test
    public void testValidWithdrawAmountToWithdrawSummaryScreen() {
        AccountDto account = createAccount(BALANCE_AMOUNT);
        sessionContext.setSession(account);
        setUserInput("20\n");
        doReturn(mock(WithdrawSummaryScreen.class)).when(screenManager).getScreen(ScreenEnum.WITHDRAW_SUMMARY_SCREEN);
        Assertions.assertTrue(otherWithdrawnScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testWithdrawWithNonNumericAmountToWithdrawScreenAndGetInvalidAmountErrorMessage() {
        AccountDto account = createAccount(BALANCE_AMOUNT);
        sessionContext.setSession(account);
        setUserInput("10s\n");
        setUpSystemOutCapturer();
        doReturn(mock(WithdrawScreen.class)).when(screenManager).getScreen(ScreenEnum.WITHDRAW_SCREEN);
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
        doReturn(mock(WithdrawScreen.class)).when(screenManager).getScreen(ScreenEnum.WITHDRAW_SCREEN);
        Assertions.assertTrue(otherWithdrawnScreen.display() instanceof WithdrawScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        Assertions.assertTrue(account.getBalance().toString().equals("$100.0"));
        closeSystemOutCapturer();
    }
}