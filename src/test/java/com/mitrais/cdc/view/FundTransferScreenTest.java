package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.mapper.AccountMapper;
import com.mitrais.cdc.repository.AccountRepository;
import com.mitrais.cdc.repository.TransactionRepository;
import com.mitrais.cdc.service.*;
import com.mitrais.cdc.service.impl.*;
import com.mitrais.cdc.util.SessionContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

@ActiveProfiles("test")
@DataJpaTest
public class FundTransferScreenTest {
    private static final double BALANCE_AMOUNT = 100.0;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private FundTransferScreen fundTransferScreen;
    private SessionContext sessionContext = new SessionContext();
    private AccountTransactionService accountTransactionService;
    private AccountValidatorService accountValidatorService = AccountValidatorServiceImplTest.getAccountValidatorServiceImpl();
    private UserInputService userInputService = UserInputServiceImplTest.getUserInputService();
    private TransactionAmountValidatorService transactionAmountValidatorService = TransactionAmountValidatorServiceImplTest.getTransactionAmountValidatorService();
    private SearchAccountService searchAccountService;
    private FundTransferSummaryScreen fundTransferSummaryScreen = Mockito.mock(FundTransferSummaryScreen.class);


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private void setUpSystemOutCapturer() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    private void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    @BeforeEach
    public void setUp() {
        accountTransactionService = AccountTransactionServiceImplTest.getAccountTransactionService(transactionRepository, accountRepository);
        searchAccountService = SearchAccountServiceImplTest.getSearchAccountService(accountRepository);
        accountRepository.save(new Account("112233", "TEST01", "012108", new Dollar(BALANCE_AMOUNT)));
        accountRepository.save(new Account("112244", "TEST02", "012109", new Dollar(BALANCE_AMOUNT)));
    }

    @AfterEach
    public void deleteData() {
        accountRepository.deleteAll();
    }

    private AccountDto createAccount() {
        Account account = accountRepository.findAccountByAccountNumber("112233");
        return AccountMapper.toAccountDto(account);
    }

    private void setUserInput(AccountDto sourceAccount, String menuInput) {
        ByteArrayInputStream userInput = new ByteArrayInputStream(menuInput.getBytes());
        System.setIn(userInput);
        fundTransferScreen = new FundTransferScreen(accountValidatorService, sessionContext, accountTransactionService, userInputService, transactionAmountValidatorService, searchAccountService, fundTransferSummaryScreen, new Scanner(System.in));
        sessionContext.setSession(sourceAccount);
        fundTransferScreen.setPreviousScreen(Mockito.mock(TransactionScreen.class));
    }

    @Test
    public void testDisplayWithValidInputToFundTransferSummaryScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "112244\n50\n\n1\n");
        Screen nextScreen = fundTransferScreen.display();
        Assertions.assertTrue(nextScreen instanceof FundTransferSummaryScreen);
    }

    @Test
    public void testDisplayWithEscForAccountNumberInput() {
        AccountDto account = createAccount();
        setUserInput(account, "Esc\n");
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testWithEmptyAccountNumberGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "\n");
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
    }

    @Test
    public void testWithNonNumericAccountNumberGetErrorMessageAndGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "dsda\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid Account"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithIncorrectAccountNumberLengthGetErrorMessageAndGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "1231\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Account Number should have 6 digits length"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithUnregisteredAccountNumberGetErrorMessageAndGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "123111\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid Account"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithEmptyAmountGetErrorMessageAndGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "112244\n\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(!outputStreamCaptor.toString().contains("Invalid"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithNonNumericAmountGetErrorMessageAndGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "112244\nss\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid amount"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithLessThanOneAmountGetErrorMessageAndGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "112244\n0\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Minimum amount to transfer is $1"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithAmountMoreThanAccountBalanceGetErrorMessageAndGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "112244\n110\n\n1\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Insufficient balance $110"));
        closeSystemOutCapturer();
    }

    @Test
    public void testWithMoreThan1000AmountGetErrorMessageAndGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "112244\n1001\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Maximum amount to transfer is $1000"));
        closeSystemOutCapturer();
    }

    @Test
    public void testSuccessTransferCorrectSourceAndDestinationAccountBalance() throws Exception {
        AccountDto account = createAccount();
        setUserInput(account, "112244\n50\n\n1\n");
        fundTransferScreen.display();

        Account sourceAccount = accountRepository.findAccountByAccountNumber("112233");
        Account destinationAccount = accountRepository.findAccountByAccountNumber("112244");

        Assertions.assertEquals(new Dollar(BALANCE_AMOUNT - 50.0).toString(), sourceAccount.getBalance().toString());
        Assertions.assertEquals(new Dollar(BALANCE_AMOUNT + 50.0).toString(), destinationAccount.getBalance().toString());
    }

    @Test
    public void testInputNonEmptyStringOnReferencePromptSceenAngGotoTransactionScreen() {
        AccountDto account = createAccount();
        setUserInput(account, "112244\n50\nss\n");
        Screen nextScreen = fundTransferScreen.display();
        Assertions.assertTrue(nextScreen instanceof TransactionScreen);
    }

    @Test
    public void testCancelTrxAtConfirmationScreenGoToTransaction() {
        AccountDto account = createAccount();
        setUserInput(account, "112244\n10\n\n2\n");
        setUpSystemOutCapturer();
        Assertions.assertTrue(fundTransferScreen.display() instanceof TransactionScreen);
        closeSystemOutCapturer();
    }
}