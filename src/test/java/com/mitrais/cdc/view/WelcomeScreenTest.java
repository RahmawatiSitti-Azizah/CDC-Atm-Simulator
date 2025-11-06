package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.repository.AccountRepository;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.impl.AccountValidatorServiceImplTest;
import com.mitrais.cdc.service.impl.SearchAccountServiceImplTest;
import com.mitrais.cdc.util.SessionContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ActiveProfiles(value = "test")
@DataJpaTest
public class WelcomeScreenTest {

    private static final double BALANCE_AMOUNT = 100.0;
    private WelcomeScreen screenInTest;
    private SessionContext sessionContext = spy(SessionContext.class);
    private AccountValidatorService validatorService = spy(AccountValidatorServiceImplTest.getAccountValidatorServiceImpl());
    private SearchAccountService searchAccountService;
    private ScreenManager screenManager = mock(ScreenManager.class);

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        searchAccountService = SearchAccountServiceImplTest.getSearchAccountService(accountRepository);
        accountRepository.save(new Account("113333", "TEST01", "012108", new Dollar(BALANCE_AMOUNT)));
        accountRepository.save(new Account("113344", "TEST02", "012109", new Dollar(BALANCE_AMOUNT)));
    }

    @AfterEach
    public void clear() {
        accountRepository.delete(new Account("113333", "TEST01", "012108", new Dollar(BALANCE_AMOUNT)));
        accountRepository.delete(new Account("113344", "TEST02", "012109", new Dollar(BALANCE_AMOUNT)));
    }

    public void setUserInput(String input) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(userInput);
        screenInTest = new WelcomeScreen(searchAccountService, sessionContext, validatorService, screenManager, new Scanner(System.in));
        doReturn(mock(TransactionScreen.class)).when(screenManager).getScreen(ScreenEnum.TRANSACTION_SCREEN);
    }

    @Test
    public void testDisplayReturnTransactionScreen() {
        setUserInput("113333\n012108");
        Assertions.assertTrue(screenInTest.display() instanceof TransactionScreen);
    }

    @Test
    public void testDisplayWithCorrectAccountButIncorrectPin() {
        setUserInput("113333\n012109");
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithAccountLengthLessThan6() {
        setUserInput("11223\n012109");
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithAccountLengthMoreThan6() {
        setUserInput("1122334\n012109");
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithAccountNotNumber() {
        setUserInput("tfghhj\n012109");
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithPinNotNumber() {
        setUserInput("113333\nfassaa");
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithPinLengthLessThan6() {
        setUserInput("113333\n01210");
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithPinLengthMoreThan6() {
        setUserInput("113333\n0121088");
        Assertions.assertTrue(screenInTest.display() instanceof WelcomeScreen);
    }
}