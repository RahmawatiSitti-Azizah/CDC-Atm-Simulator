package com.mitrais.cdc.view;

import com.mitrais.cdc.cli.SessionContext;
import com.mitrais.cdc.cli.view.*;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.UserInputServiceImplTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class WithdrawScreenTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private WithdrawScreen withdrawScreen;
    private ScreenManager screenManager = mock(ScreenManager.class);
    private SessionContext sessionContext = new SessionContext();
    private UserInputService userInputService = UserInputServiceImplTest.getUserInputService();

    public void setUserInput(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        withdrawScreen = new WithdrawScreen(screenManager, userInputService, new Scanner(System.in), sessionContext);
        sessionContext.setSession(mock(AccountDto.class));
    }

    @Test
    public void testDisplayWithdrawMenu1toWithdrawSummaryScreen() {
        setUserInput("1\n");
        doReturn(mock(WithdrawSummaryScreen.class)).when(screenManager).getScreen(ScreenEnum.WITHDRAW_SUMMARY_SCREEN);
        Assertions.assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testDisplayWithdrawMenu2toWithdrawSummaryScreen() {
        setUserInput("2\n");
        doReturn(mock(WithdrawSummaryScreen.class)).when(screenManager).getScreen(ScreenEnum.WITHDRAW_SUMMARY_SCREEN);
        Assertions.assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testDisplayWithdrawMenu3toWithdrawSummaryScreen() {
        setUserInput("3\n");
        doReturn(mock(WithdrawSummaryScreen.class)).when(screenManager).getScreen(ScreenEnum.WITHDRAW_SUMMARY_SCREEN);
        Assertions.assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testDisplayWithdrawMenutoOtherAmountScreen() {
        setUserInput("4\n");
        doReturn(mock(OtherWithdrawnScreen.class)).when(screenManager).getScreen(ScreenEnum.OTHER_WITHDRAW_SCREEN);
        Assertions.assertTrue(withdrawScreen.display() instanceof OtherWithdrawnScreen);
    }

    @Test
    public void testDisplayWithdrawMenuExit() {
        setUserInput("5\n");
        doReturn(mock(TransactionScreen.class)).when(screenManager).getScreen(ScreenEnum.TRANSACTION_SCREEN);
        Assertions.assertTrue(withdrawScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testDisplayWithdrawInvalidMenu() {
        setUserInput("s\n");
        doReturn(mock(TransactionScreen.class)).when(screenManager).getScreen(ScreenEnum.TRANSACTION_SCREEN);
        Assertions.assertTrue(withdrawScreen.display() instanceof TransactionScreen);
    }
}