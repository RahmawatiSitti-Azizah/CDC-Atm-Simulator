package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.dto.TransactionDto;
import com.mitrais.cdc.model.mapper.AccountMapper;
import com.mitrais.cdc.model.mapper.TransactionMapper;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.TransactionService;
import com.mitrais.cdc.util.ErrorConstant;
import com.mitrais.cdc.web.controller.TransactionController;
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
import java.util.List;

class TransactionControllerTest {
    private static final double BALANCE_AMOUNT = 200.0;
    private static final Dollar TRANSFER_AMOUNT = new Dollar(10.0);
    private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.now();

    MockMvc mockMvc;

    @InjectMocks
    private TransactionController controllerInTest;

    @Mock
    private TransactionService transactionService;

    @Mock
    private SearchAccountService searchAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controllerInTest).build();
    }

    @Test
    public void testHistoryMenu_returnHistoryMenuPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/transaction"))
                .andExpect(MockMvcResultMatchers.view().name("HistoryMenu"));
    }

    @Test
    public void testProcessTransaction_whenAllParameterValid_thenReturnHistoryListPage() throws Exception {
        AccountDto account = new AccountDto("Jane Doe", "111111", new Dollar(BALANCE_AMOUNT));
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Account destination = new Account("111111", "John Doe", null, new Dollar(250.0));
        Transaction transferTransaction = new Transaction(null, AccountMapper.toEntity(account), destination, TRANSFER_AMOUNT, "123sads", "Transfer", TRANSACTION_DATE);
        Transaction withdrawTransaction = new Transaction(null, AccountMapper.toEntity(account), null, TRANSFER_AMOUNT, "", "Withdraw", TRANSACTION_DATE);
        List<TransactionDto> listTransaction = List.of(TransactionMapper.toTransactionDto(withdrawTransaction), TransactionMapper.toTransactionDto(transferTransaction));
        Mockito.when(transactionService.getTransactionHistoryAccount(Mockito.any(String.class), Mockito.any(Integer.class))).thenReturn(listTransaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .param("max", "10"))
                .andExpect(MockMvcResultMatchers.view().name("HistoryList"))
                .andExpect(MockMvcResultMatchers.model().attribute("transaction", listTransaction));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(transactionService, Mockito.times(1)).getTransactionHistoryAccount(Mockito.any(String.class), Mockito.any(Integer.class));
    }

    @Test
    public void testProcessTransaction_whenInvalidParameter_thenReturnHistoryMenuPageWithErrorMessage() throws Exception {
        AccountDto account = new AccountDto("Jane Doe", "111111", new Dollar(BALANCE_AMOUNT));
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Account destination = new Account("111111", "John Doe", null, new Dollar(250.0));
        Transaction transferTransaction = new Transaction(null, AccountMapper.toEntity(account), destination, TRANSFER_AMOUNT, "123sads", "Transfer", TRANSACTION_DATE);
        Transaction withdrawTransaction = new Transaction(null, AccountMapper.toEntity(account), null, TRANSFER_AMOUNT, "", "Withdraw", TRANSACTION_DATE);
        List<TransactionDto> listTransaction = List.of(TransactionMapper.toTransactionDto(withdrawTransaction), TransactionMapper.toTransactionDto(transferTransaction));
        Mockito.when(transactionService.getTransactionHistoryAccount(Mockito.any(String.class), Mockito.any(Integer.class))).thenReturn(listTransaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .param("max", "xxx"))
                .andExpect(MockMvcResultMatchers.view().name("HistoryMenu"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.INVALID_MAX_TRANSACTION_SHOWN));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(transactionService, Mockito.never()).getTransactionHistoryAccount(Mockito.any(String.class), Mockito.any(Integer.class));
    }

    @Test
    public void testProcessTransaction_whenInvalidSessionAccount_thenReturnLoginPageAndErrorMessage() throws Exception {
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenThrow(new EntityNotFoundException(ErrorConstant.ACCOUNT_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .param("max", "xxx"))
                .andExpect(MockMvcResultMatchers.view().name("Login"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.ACCOUNT_NOT_FOUND));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(transactionService, Mockito.never()).getTransactionHistoryAccount(Mockito.any(String.class), Mockito.any(Integer.class));
    }
}