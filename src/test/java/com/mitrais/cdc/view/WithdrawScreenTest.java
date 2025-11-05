package com.mitrais.cdc.view;

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

public class WithdrawScreenTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private WithdrawScreen withdrawScreen;
    private OtherWithdrawnScreen otherWithdrawnScreen = mock(OtherWithdrawnScreen.class);
    private WithdrawSummaryScreen withdrawSummaryScreen = mock(WithdrawSummaryScreen.class);
    private SessionContext sessionContext = new SessionContext();
    private UserInputService userInputService = UserInputServiceImplTest.getUserInputService();

    public void setUserInput(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        withdrawScreen = new WithdrawScreen(otherWithdrawnScreen, withdrawSummaryScreen, userInputService, new Scanner(System.in), sessionContext);
        sessionContext.setSession(mock(AccountDto.class));
        withdrawScreen.setPreviousScreen(mock(TransactionScreen.class));
    }

    @Test
    public void testDisplayWithdrawMenu1toWithdrawSummaryScreen() {
        setUserInput("1\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testDisplayWithdrawMenu2toWithdrawSummaryScreen() {
        setUserInput("2\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testDisplayWithdrawMenu3toWithdrawSummaryScreen() {
        setUserInput("3\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof WithdrawSummaryScreen);
    }

    @Test
    public void testDisplayWithdrawMenutoOtherAmountScreen() {
        setUserInput("4\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof OtherWithdrawnScreen);
    }

    @Test
    public void testDisplayWithdrawMenuExit() {
        setUserInput("5\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testDisplayWithdrawInvalidMenu() {
        setUserInput("s\n");
        Assertions.assertTrue(withdrawScreen.display() instanceof TransactionScreen);
    }
}