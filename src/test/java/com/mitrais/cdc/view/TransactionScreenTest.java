package com.mitrais.cdc.view;

import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.UserInputServiceImplTest;
import com.mitrais.cdc.util.SessionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ActiveProfiles(value = "test")
public class TransactionScreenTest {

    private TransactionScreen screenInTest;
    private SessionContext sessionContext = spy(SessionContext.class);
    private UserInputService userInputService = spy(UserInputServiceImplTest.getUserInputService());
    private WithdrawScreen withdrawScreen = mock();
    private FundTransferScreen fundTransferScreen = mock();
    private HistoryTransactionScreen historyTransactionScreen = mock();

    public void setUserInput(String input) {
        sessionContext.setSession(mock(AccountDto.class));
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        screenInTest = new TransactionScreen(sessionContext, new Scanner(System.in), userInputService, withdrawScreen, fundTransferScreen, historyTransactionScreen);
    }

    @Test
    public void testDisplayMenu1() {
        setUserInput("1\n");
        Assertions.assertTrue(screenInTest.display() instanceof WithdrawScreen);
    }

    @Test
    public void testDisplayMenu2() {
        setUserInput("2\n");
        Assertions.assertTrue(screenInTest.display() instanceof FundTransferScreen);
    }

    @Test
    public void testDisplayMenu3() {
        setUserInput("3\n");
        Assertions.assertTrue(screenInTest.display() instanceof HistoryTransactionScreen);
    }

    @Test
    public void testDisplayMenu4() {
        setUserInput("4\n");
        screenInTest.setPreviousScreen(mock(WelcomeScreen.class));
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayInvalidMenuInput() {
        setUserInput("hgjhg\n");
        screenInTest.setPreviousScreen(mock(WelcomeScreen.class));
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }
}