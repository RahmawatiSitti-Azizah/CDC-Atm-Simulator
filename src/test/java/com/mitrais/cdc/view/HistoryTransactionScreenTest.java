package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.mapper.TransactionMapper;
import com.mitrais.cdc.service.TransactionService;
import com.mitrais.cdc.util.SessionContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryTransactionScreenTest {
    private static final double BALANCE_AMOUNT = 100.0;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private HistoryTransactionScreen historyTransactionScreen;
    private SessionContext sessionContext = new SessionContext();
    private ScreenManager screenManager = Mockito.mock(ScreenManager.class);
    private TransactionService transactionService = Mockito.mock(TransactionService.class);

    @BeforeEach
    public void setUp() {
        historyTransactionScreen = new HistoryTransactionScreen(sessionContext, screenManager, transactionService);
        sessionContext.setSession(new AccountDto("TEST 02", "112288", new Dollar(100.0)));
        System.setOut(new PrintStream(outputStreamCaptor));
        Mockito.doReturn(Mockito.mock(TransactionScreen.class)).when(screenManager).getScreen(ScreenEnum.TRANSACTION_SCREEN);
    }

    @AfterEach
    public void closeSystemOutCapturer() {
        System.setOut(standardOut);
    }

    @Test
    public void testDisplayWithTransactionHistoryThenShouldPrintTransactionAndGoToTransactionScreen() {
        Account account = new Account("112288", "TEST 02", null, new Dollar(BALANCE_AMOUNT));
        Account destination = new Account("111111", "John Doe", null, new Dollar(250.0));
        Transaction transferTransaction = new Transaction(null, account, destination, new Dollar(20.0), "123sads", "Transfer", LocalDateTime.now());
        Transaction withdrawTransaction = new Transaction(null, account, null, new Dollar(20.0), "", "Withdraw", LocalDateTime.now());
        Transaction incomingTransferTransaction = new Transaction(null, destination, account, new Dollar(20.0), "123sads", "Transfer", LocalDateTime.now());
        Mockito.doReturn(List.of(TransactionMapper.toTransactionDto(transferTransaction), TransactionMapper.toTransactionDto(withdrawTransaction), TransactionMapper.toTransactionDto(incomingTransferTransaction))).when(transactionService).getTransactionHistoryAccount(Mockito.anyString(), Mockito.anyInt());
        Assertions.assertTrue(historyTransactionScreen.display() instanceof TransactionScreen);
        String outputString = outputStreamCaptor.toString();
        Assertions.assertTrue(outputString.contains("Withdraw") || outputString.contains("Transfer"));
        closeSystemOutCapturer();
    }

    @Test
    public void testDisplayWithNoTransactionHistoryThenShouldPrintNoTransactionMessageAndGoToTransactionScreen() {
        Mockito.doReturn(List.of()).when(transactionService).getTransactionHistoryAccount(Mockito.anyString(), Mockito.anyInt());
        Assertions.assertTrue(historyTransactionScreen.display() instanceof TransactionScreen);
        String outputString = outputStreamCaptor.toString();
        Assertions.assertTrue(outputString.contains("No transaction history found for account: 112288"));
        closeSystemOutCapturer();
    }
}