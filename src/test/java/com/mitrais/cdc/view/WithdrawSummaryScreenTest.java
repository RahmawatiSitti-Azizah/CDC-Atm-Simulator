package com.mitrais.cdc.view;

import com.mitrais.cdc.cli.SessionContext;
import com.mitrais.cdc.cli.view.*;
import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.mapper.AccountMapper;
import com.mitrais.cdc.repository.AccountRepository;
import com.mitrais.cdc.repository.TransactionRepository;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.service.TransactionAmountValidatorService;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.AccountTransactionServiceImplTest;
import com.mitrais.cdc.service.impl.TransactionAmountValidatorServiceImplTest;
import com.mitrais.cdc.service.impl.UserInputServiceImplTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ActiveProfiles(value = "test")
@DataJpaTest
public class WithdrawSummaryScreenTest {
    private static final double BALANCE_AMOUNT = 100.0;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private WithdrawSummaryScreen withdrawSummaryScreen;
    private AccountTransactionService accountTransactionService;
    private TransactionAmountValidatorService transactionAmountValidatorService = spy(TransactionAmountValidatorServiceImplTest.getTransactionAmountValidatorService());
    private UserInputService userInputService = UserInputServiceImplTest.getUserInputService();
    private SessionContext sessionContext = new SessionContext();
    private ScreenManager screenManager = mock(ScreenManager.class);


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        accountTransactionService = AccountTransactionServiceImplTest.getAccountTransactionService(transactionRepository, accountRepository);
        accountRepository.save(new Account("113333", "TEST01", "012108", new Dollar(BALANCE_AMOUNT)));
        accountRepository.save(new Account("113344", "TEST02", "012109", new Dollar(BALANCE_AMOUNT)));
        doReturn(mock(TransactionScreen.class)).when(screenManager).getScreen(ScreenEnum.TRANSACTION_SCREEN);
        doReturn(mock(WelcomeScreen.class)).when(screenManager).getScreen(ScreenEnum.WELCOME_SCREEN);
    }

    @AfterEach
    public void clear() {
        accountRepository.delete(new Account("113333", "TEST01", "012108", new Dollar(BALANCE_AMOUNT)));
        accountRepository.delete(new Account("113344", "TEST02", "012109", new Dollar(BALANCE_AMOUNT)));
    }

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    private Account createAccount() {
        return accountRepository.findAccountByAccountNumber("113333");
    }

    private void setUserInput(AccountDto sourceAccount, double amount, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        withdrawSummaryScreen = new WithdrawSummaryScreen(accountTransactionService, transactionAmountValidatorService, userInputService, sessionContext, new Scanner(System.in), screenManager);
        sessionContext.setSession(sourceAccount);
        withdrawSummaryScreen.setTransactionAmount(new Dollar(amount));
    }

    @Test
    public void testDisplayWith1Input_thenReturnTransactionScreen() {
        Account account = createAccount();
        setUserInput(AccountMapper.toAccountDto(account), 20, "1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assertions.assertTrue(nextScreen instanceof TransactionScreen);
    }

    @Test
    public void testDisplayWith2Input_thenReturnWelcomeScreen() {
        Account account = createAccount();
        setUserInput(AccountMapper.toAccountDto(account), 20, "2\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assertions.assertTrue(nextScreen instanceof WelcomeScreen);
    }

    @Test
    public void testOtherInput_thenReturnWelcomeScreen() {
        Account account = createAccount();
        setUserInput(AccountMapper.toAccountDto(account), 20, "as1\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assertions.assertTrue(nextScreen instanceof WelcomeScreen);
    }

    @Test
    public void testDisplayWithNonNumericInput_thenReturnWelcomeScreen() {
        Account account = createAccount();
        setUserInput(AccountMapper.toAccountDto(account), 20, "jj\n");
        Screen nextScreen = withdrawSummaryScreen.display();
        Assertions.assertTrue(nextScreen instanceof WelcomeScreen);
    }

    @Test
    public void testWithdrawWithValidAmount_thenAccountBalanceDecrease() throws Exception {
        Account beforeAccount = createAccount();
        setUserInput(AccountMapper.toAccountDto(beforeAccount), 20, "jj\n");
        withdrawSummaryScreen.display();
        Account afterAccount = createAccount();
        Assertions.assertTrue(beforeAccount.getStringBalance().equals(afterAccount.getStringBalance()));
    }

    @Test
    public void testWithdrawWithNonMultipleOf10Amount_thenPrintErrorMessage() {
        Account account = createAccount();
        String initialBalance = account.getStringBalance();
        setUserInput(AccountMapper.toAccountDto(account), 5, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        Assertions.assertTrue(account.getStringBalance().equals(initialBalance));
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithdrawWithExceedMaximumAmount_thenPrintErrorMessage() {
        Account account = createAccount();
        String initialBalance = account.getStringBalance();
        setUserInput(AccountMapper.toAccountDto(account), 1010, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        Assertions.assertTrue(account.getStringBalance().equals(initialBalance));
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Maximum amount to transfer is $1000"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithdrawWithAmountExceedAccountBalance_thenPrintErrorMessage() {
        Account account = createAccount();
        String initialBalance = account.getStringBalance();
        setUserInput(AccountMapper.toAccountDto(account), 500, "jj\n");
        setUpSystemOutCapturer();
        withdrawSummaryScreen.display();
        Assertions.assertTrue(account.getStringBalance().equals(initialBalance));
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Insufficient balance $500"));
        closeSystemOutCapturer();
    }
}