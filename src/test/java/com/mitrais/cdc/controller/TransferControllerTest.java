package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.TransactionAmountValidatorService;
import com.mitrais.cdc.util.ErrorConstant;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class TransferControllerTest {
    private static final Dollar TRANSFER_AMOUNT = new Dollar(10.0);
    private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.now();

    MockMvc mockMvc;

    @InjectMocks
    TransferController controllerInTest;

    @Mock
    AccountTransactionService transactionService;

    @Mock
    SearchAccountService searchAccountService;

    @Mock
    TransactionAmountValidatorService amountValidatorService;

    @Mock
    AccountValidatorService accountValidatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controllerInTest).build();
    }

    @Test
    public void testTransferMenu_thenReturnTransferMenuPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/transfer"))
                .andExpect(MockMvcResultMatchers.view().name("TransferMenu"));
    }

    @Test
    public void testProcessTransfer_whenAllParameterValid_thenReturnTransferSummaryPage() throws Exception {
        Account account = new Account(new Dollar(200.0), "Jane Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Account destination = new Account(new Dollar(250.0), "John Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.anyString())).thenReturn(destination);
        Transaction transaction = new Transaction(null, account, destination, TRANSFER_AMOUNT, "123sads", "Transfer", TRANSACTION_DATE);
        Mockito.when(transactionService.transfer(Mockito.any(Account.class), Mockito.any(Account.class), Mockito.any(Money.class), Mockito.any())).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .param("amount", "10")
                        .param("destAccount", "223311"))
                .andExpect(MockMvcResultMatchers.view().name("TransferSummary"))
                .andExpect(MockMvcResultMatchers.model().attribute("amount", transaction.getAmount()))
                .andExpect(MockMvcResultMatchers.model().attribute("balance", account.getStringBalance()))
                .andExpect(MockMvcResultMatchers.model().attribute("transactionDate", transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(accountValidatorService, Mockito.times(1)).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.anyString());
        Mockito.verify(amountValidatorService, Mockito.times(1)).validateTransferAmount(Mockito.any());
        Mockito.verify(transactionService, Mockito.times(1)).transfer(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testProcessTransfer_whenInvalidSessionAccount_thenReturnTransferMenuPageWithErrorMessage() throws Exception {
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenThrow(new EntityNotFoundException(ErrorConstant.ACCOUNT_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .param("amount", "10")
                        .param("destAccount", "223311"))
                .andExpect(MockMvcResultMatchers.view().name("Login"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.ACCOUNT_NOT_FOUND));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(accountValidatorService, Mockito.never()).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.never()).get(Mockito.anyString());
        Mockito.verify(amountValidatorService, Mockito.never()).validateTransferAmount(Mockito.any());
        Mockito.verify(transactionService, Mockito.never()).transfer(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testProcessTransfer_whenInvalidDestinationAccount_thenReturnTransferMenuPageAndErrorMessage() throws Exception {
        Account account = new Account(new Dollar(200.0), "Jane Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Mockito.doThrow(new Exception(ErrorConstant.ACCOUNT_NUMBER_SHOULD_HAVE_6_DIGITS_LENGTH)).when(accountValidatorService).validateAccountNumber(Mockito.any(), Mockito.anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .param("amount", "10")
                        .param("destAccount", "223311"))
                .andExpect(MockMvcResultMatchers.view().name("TransferMenu"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.ACCOUNT_NUMBER_SHOULD_HAVE_6_DIGITS_LENGTH));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(accountValidatorService, Mockito.times(1)).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.never()).get(Mockito.anyString());
        Mockito.verify(amountValidatorService, Mockito.never()).validateTransferAmount(Mockito.any());
        Mockito.verify(transactionService, Mockito.never()).transfer(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testProcessTransfer_whenDestinationAccountNotFound_thenReturnTransferMenuPageWithErrorMessage() throws Exception {
        Account account = new Account(new Dollar(200.0), "Jane Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Mockito.when(searchAccountService.get(Mockito.anyString())).thenThrow(new Exception(ErrorConstant.INVALID_ACCOUNT));

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .param("amount", "10")
                        .param("destAccount", "223311"))
                .andExpect(MockMvcResultMatchers.view().name("TransferMenu"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.INVALID_ACCOUNT));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(accountValidatorService, Mockito.times(1)).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.anyString());
        Mockito.verify(amountValidatorService, Mockito.never()).validateTransferAmount(Mockito.any());
        Mockito.verify(transactionService, Mockito.never()).transfer(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testProcessTransfer_whenInvalidAmount_thenReturnTransferMenuPageWithErrorMessage() throws Exception {
        Account account = new Account(new Dollar(200.0), "Jane Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Account destination = new Account(new Dollar(250.0), "John Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.anyString())).thenReturn(destination);
        Mockito.doThrow(new Exception(ErrorConstant.MINIMUM_AMOUNT_ERROR_MESSAGE)).when(amountValidatorService).validateTransferAmount(Mockito.any(Money.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .param("amount", "10")
                        .param("destAccount", "223311"))
                .andExpect(MockMvcResultMatchers.view().name("TransferMenu"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.MINIMUM_AMOUNT_ERROR_MESSAGE));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(accountValidatorService, Mockito.times(1)).validateAccountNumber(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.anyString());
        Mockito.verify(amountValidatorService, Mockito.times(1)).validateTransferAmount(Mockito.any());
        Mockito.verify(transactionService, Mockito.never()).transfer(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }
}