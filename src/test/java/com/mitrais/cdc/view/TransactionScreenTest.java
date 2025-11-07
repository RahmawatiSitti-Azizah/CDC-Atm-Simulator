package com.mitrais.cdc.view;

import com.mitrais.cdc.cli.SessionContext;
import com.mitrais.cdc.cli.view.*;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.UserInputServiceImplTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ActiveProfiles(value = "test")
public class TransactionScreenTest {

    private TransactionScreen screenInTest;
    private SessionContext sessionContext = spy(SessionContext.class);
    private UserInputService userInputService = spy(UserInputServiceImplTest.getUserInputService());
    private ScreenManager screenManager = mock(ScreenManager.class);

    public void setUserInput(String input) {
        sessionContext.setSession(mock(AccountDto.class));
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        screenInTest = new TransactionScreen(sessionContext, new Scanner(System.in), userInputService, screenManager);
    }

    @Test
    public void testDisplayMenu1() {
        setUserInput("1\n");
        doReturn(mock(WithdrawScreen.class)).when(screenManager).getScreen(ScreenEnum.WITHDRAW_SCREEN);
        Assertions.assertTrue(screenInTest.display() instanceof WithdrawScreen);
    }

    @Test
    public void testDisplayMenu2() {
        setUserInput("2\n");
        doReturn(mock(FundTransferScreen.class)).when(screenManager).getScreen(ScreenEnum.FUND_TRANSFER_SCREEN);
        Assertions.assertTrue(screenInTest.display() instanceof FundTransferScreen);
    }

    @Test
    public void testDisplayMenu3() {
        setUserInput("3\n");
        doReturn(mock(HistoryTransactionScreen.class)).when(screenManager).getScreen(ScreenEnum.HISTORY_TRANSACTION_SCREEN);
        Assertions.assertTrue(screenInTest.display() instanceof HistoryTransactionScreen);
    }

    @Test
    public void testDisplayMenu4() {
        setUserInput("4\n");
        doReturn(mock(WelcomeScreen.class)).when(screenManager).getScreen(ScreenEnum.WELCOME_SCREEN);
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayInvalidMenuInput() {
        setUserInput("hgjhg\n");
        doReturn(mock(WelcomeScreen.class)).when(screenManager).getScreen(ScreenEnum.WELCOME_SCREEN);
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }
}