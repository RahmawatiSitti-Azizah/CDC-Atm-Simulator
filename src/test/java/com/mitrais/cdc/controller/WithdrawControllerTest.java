package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.service.AccountTransactionService;
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

class WithdrawControllerTest {
    private static final Dollar WITHDRAW_AMOUNT = new Dollar(10.0);
    private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.now();
    private MockMvc mockMvc;

    @InjectMocks
    private WithdrawController controllerInTest;

    @Mock
    private AccountTransactionService accountTransactionService;

    @Mock
    private TransactionAmountValidatorService amountValidatorService;

    @Mock
    private SearchAccountService searchAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controllerInTest).build();
    }

    @Test
    public void testWithdrawMenu_thenReturnWithdrawMenuPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/withdraw"))
                .andExpect(MockMvcResultMatchers.view().name("WithdrawMenu"));
    }

    @Test
    public void testProcessWithdraw_whenAllParameterValid_thenReturnWithdrawSummaryPage() throws Exception {
        Account account = new Account(new Dollar(200.0), "Jane Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Transaction transaction = new Transaction(null, account, null, WITHDRAW_AMOUNT, "", "Withdraw", TRANSACTION_DATE);
        Mockito.when(accountTransactionService.withdraw(Mockito.any(Account.class), Mockito.any(Money.class))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                        .param("amount", "10"))
                .andExpect(MockMvcResultMatchers.view().name("WithdrawSummary"))
                .andExpect(MockMvcResultMatchers.model().attribute("withdraw", WITHDRAW_AMOUNT))
                .andExpect(MockMvcResultMatchers.model().attribute("balance", account.getStringBalance()))
                .andExpect(MockMvcResultMatchers.model().attribute("transactionDate", transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(amountValidatorService, Mockito.times(1)).validateWithdrawAmount(Mockito.any(Money.class));
        Mockito.verify(accountTransactionService, Mockito.times(1)).withdraw(Mockito.any(Account.class), Mockito.any(Money.class));
    }

    @Test
    public void testProcessWithdraw_whenNoSessionAccountAvailable_thenReturnLoginPage() throws Exception {
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenThrow(new EntityNotFoundException(ErrorConstant.ACCOUNT_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                        .param("amount", "10"))
                .andExpect(MockMvcResultMatchers.view().name("Login"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.ACCOUNT_NOT_FOUND));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(amountValidatorService, Mockito.never()).validateWithdrawAmount(Mockito.any(Money.class));
        Mockito.verify(accountTransactionService, Mockito.never()).withdraw(Mockito.any(Account.class), Mockito.any(Money.class));
    }

    @Test
    public void testProcessWithdraw_whenInvalidAmountAndFromWithdrawMenu_thenReturnWithdrawMenu() throws Exception {
        Account account = new Account(new Dollar(200.0), "Jane Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Mockito.doThrow(new Exception(ErrorConstant.INVALID_AMOUNT)).when(amountValidatorService).validateWithdrawAmount(Mockito.any(Money.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                        .param("amount", "10"))
                .andExpect(MockMvcResultMatchers.view().name("WithdrawMenu"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.INVALID_AMOUNT));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(amountValidatorService, Mockito.times(1)).validateWithdrawAmount(Mockito.any(Money.class));
        Mockito.verify(accountTransactionService, Mockito.never()).withdraw(Mockito.any(Account.class), Mockito.any(Money.class));
    }

    @Test
    public void testProcessWithdraw_whenInvalidAmountAndFromOtherWithdrawMenu_thenReturnOtherWithdrawMenu() throws Exception {
        Account account = new Account(new Dollar(200.0), "Jane Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Mockito.doThrow(new Exception(ErrorConstant.INVALID_AMOUNT)).when(amountValidatorService).validateWithdrawAmount(Mockito.any(Money.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                        .param("amount", "10")
                        .param("other", "true"))
                .andExpect(MockMvcResultMatchers.view().name("OtherWithdrawMenu"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.INVALID_AMOUNT));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(amountValidatorService, Mockito.times(1)).validateWithdrawAmount(Mockito.any(Money.class));
        Mockito.verify(accountTransactionService, Mockito.never()).withdraw(Mockito.any(Account.class), Mockito.any(Money.class));
    }

    @Test
    public void testProcessWithdraw_whenWithdrawProcessError_thenReturnPreviousPage() throws Exception {
        Account account = new Account(new Dollar(200.0), "Jane Doe", "111111", null);
        Mockito.when(searchAccountService.get(Mockito.any(HttpServletRequest.class))).thenReturn(account);
        Mockito.when(accountTransactionService.withdraw(Mockito.any(Account.class), Mockito.any(Money.class))).thenThrow(new Exception(ErrorConstant.getInsufficientBalanceErrorMessage(WITHDRAW_AMOUNT)));

        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                        .param("amount", "10"))
                .andExpect(MockMvcResultMatchers.view().name("WithdrawMenu"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.getInsufficientBalanceErrorMessage(WITHDRAW_AMOUNT)));

        Mockito.verify(searchAccountService, Mockito.times(1)).get(Mockito.any(HttpServletRequest.class));
        Mockito.verify(amountValidatorService, Mockito.times(1)).validateWithdrawAmount(Mockito.any(Money.class));
        Mockito.verify(accountTransactionService, Mockito.times(1)).withdraw(Mockito.any(Account.class), Mockito.any(Money.class));
    }

    @Test
    public void testOtherWithdrawMenu() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/withdraw/other"))
                .andExpect(MockMvcResultMatchers.view().name("OtherWithdrawMenu"));
    }
}