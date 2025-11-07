package com.mitrais.cdc.view;

import com.mitrais.cdc.cli.SessionContext;
import com.mitrais.cdc.cli.view.*;
import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.model.mapper.AccountMapper;
import com.mitrais.cdc.model.mapper.TransactionMapper;
import com.mitrais.cdc.repository.AccountRepository;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.TransactionService;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.SearchAccountServiceImplTest;
import com.mitrais.cdc.service.impl.UserInputServiceImplTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Scanner;

@ActiveProfiles("test")
@DataJpaTest
public class FundTransferSummaryScreenTest {

    private static final double TRANSFER_AMOUNT = 70.0;
    private static final String REFERENCE_NUMBER = "T1231211";
    private static final Transaction TRANSACTION = new Transaction(1, new Account("112255", "TEST01", "012108", new Dollar(100.0)), new Account("112266", "TEST02", "012109", new Dollar(100.0)), new Dollar(TRANSFER_AMOUNT), REFERENCE_NUMBER, "Transfer", LocalDateTime.now());
    private FundTransferSummaryScreen fundTransferSummaryScreen;
    private SessionContext sessionContext = new SessionContext();
    private ScreenManager screenManager = Mockito.mock(ScreenManager.class);
    private UserInputService userInputService = UserInputServiceImplTest.getUserInputService();
    private TransactionService transactionService = Mockito.mock(TransactionService.class);
    private SearchAccountService searchAccountService;

    @Autowired
    private AccountRepository accountRepository;


    @BeforeEach
    public void setUp() {
        searchAccountService = SearchAccountServiceImplTest.getSearchAccountService(accountRepository);
        accountRepository.save(new Account("112255", "TEST01", "012108", new Dollar(100.0)));
        accountRepository.save(new Account("112266", "TEST02", "012109", new Dollar(100.0)));
    }

    @AfterEach
    public void deleteData() {
        accountRepository.deleteAll();
    }

    private Account getAccount(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }

    private void setUserInput(Account sourceAccount, Account destinationAccount, double amount, String referenceNumber, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        fundTransferSummaryScreen = new FundTransferSummaryScreen(sessionContext, screenManager, userInputService, new Scanner(System.in), transactionService, searchAccountService);
        fundTransferSummaryScreen.setReferenceNumber(referenceNumber);
        sessionContext.setSession(AccountMapper.toAccountDto(sourceAccount));
        Mockito.doReturn(TransactionMapper.toTransactionDto(TRANSACTION)).when(transactionService).getTransactionByReferenceNumber(Mockito.any());
        Mockito.doReturn(Mockito.mock(WelcomeScreen.class)).when(screenManager).getScreen(ScreenEnum.WELCOME_SCREEN);
        Mockito.doReturn(Mockito.mock(TransactionScreen.class)).when(screenManager).getScreen(ScreenEnum.TRANSACTION_SCREEN);
    }

    @Test
    public void testWithMenu1GoToTransactionScreen() {
        Account sourceAccount = getAccount("112255");
        Account destinationAccount = getAccount("112266");
        setUserInput(sourceAccount, destinationAccount, TRANSFER_AMOUNT, REFERENCE_NUMBER, "1\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testWithMenu2GotoWelcomeScreen() {
        Account sourceAccount = getAccount("112255");
        Account destinationAccount = getAccount("112266");
        setUserInput(sourceAccount, destinationAccount, TRANSFER_AMOUNT, REFERENCE_NUMBER, "2\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testWithOtherInput1_thenReturnToWelcomeScreen() {
        Account sourceAccount = getAccount("112255");
        Account destinationAccount = getAccount("112266");
        setUserInput(sourceAccount, destinationAccount, TRANSFER_AMOUNT, REFERENCE_NUMBER, "3\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testWithOtherInput2_thenReturnToWelcomeScreen() {
        Account sourceAccount = getAccount("112255");
        Account destinationAccount = getAccount("112266");
        setUserInput(sourceAccount, destinationAccount, TRANSFER_AMOUNT, REFERENCE_NUMBER, "sqw11\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
    }

    @Test
    public void testWithOtherInput3_thenReturnToWelcomeScreen() {
        Account sourceAccount = getAccount("112255");
        Account destinationAccount = getAccount("112266");
        setUserInput(sourceAccount, destinationAccount, TRANSFER_AMOUNT, REFERENCE_NUMBER, "\n");
        Assertions.assertTrue(fundTransferSummaryScreen.display() instanceof WelcomeScreen);
    }
}