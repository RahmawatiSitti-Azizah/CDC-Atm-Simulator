package com.mitrais.cdc;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.dto.TransactionDto;
import com.mitrais.cdc.service.SearchAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
public class ApplicationSmokeTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    SearchAccountService searchAccountService;


    @Test
    public void testAddAccountsThenDoTransfer_checkTheTransferHistory_thenLastTransactionShouldShownOnTheList() throws Exception {
        //create 2 accounts and add into database
        String sourceAccountNumber = "112233";
        searchAccountService.addAccount(new Dollar(500.0), "Jane Doe", sourceAccountNumber, "223311");
        String destinationAccountNumber = "112244";
        searchAccountService.addAccount(new Dollar(400.0), "John Doe", destinationAccountNumber, "223344");
        //do login request to one of the account
        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("account", sourceAccountNumber)
                        .param("pin", "223311"))
                .andExpect(MockMvcResultMatchers.view().name("TransactionMenu"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //check if the loginResult contain session account data
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();
        AccountDto sessionAccount = (AccountDto) session.getAttribute("account");
        Assertions.assertNotNull(sessionAccount);
        Assertions.assertEquals(sourceAccountNumber, sessionAccount.getAccountNumber());
        Assertions.assertEquals("Jane Doe", sessionAccount.getAccountHolderName());

        //do withdraw to other account and check the response
        MvcResult withdrawResult = mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                        .param("amount", "10")
                        .session(session))
                .andExpect(MockMvcResultMatchers.view().name("WithdrawSummary"))
                .andExpect(MockMvcResultMatchers.model().attribute("balance", new Dollar(490.0).toString()))
                .andReturn();
        Dollar withdrawAmount = new Dollar(10.0);
        Assertions.assertEquals(withdrawAmount.toString(), withdrawResult.getModelAndView().getModel().get("withdraw").toString());

        //do transfer to other account and check the response
        MvcResult transferResult = mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .param("amount", "50")
                        .param("destAccount", destinationAccountNumber)
                        .session(session))
                .andExpect(MockMvcResultMatchers.view().name("TransferSummary"))
                .andExpect(MockMvcResultMatchers.model().attribute("balance", new Dollar(440.0).toString()))
                .andReturn();
        Dollar transferAmount = new Dollar(50.0);
        Assertions.assertEquals(transferAmount.toString(), transferResult.getModelAndView().getModel().get("amount").toString());

        //call transaction history and check the data from previous withdrawn and transfer process only
        MvcResult historyResult = mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .param("max", "10")
                        .session(session))
                .andExpect(MockMvcResultMatchers.view().name("HistoryList"))
                .andReturn();
        List<TransactionDto> transactionDtoList = (List<TransactionDto>) historyResult.getModelAndView().getModel().get("transaction");
        Assertions.assertEquals(2, transactionDtoList.size());
        TransactionDto dtoTransfer = transactionDtoList.get(0);
        Assertions.assertEquals("Transfer", dtoTransfer.getNote());
        Assertions.assertEquals(transferAmount.toString(), dtoTransfer.getAmount().toString());
        Assertions.assertEquals(sourceAccountNumber, dtoTransfer.getSourceAccountNumber());
        Assertions.assertEquals(destinationAccountNumber, dtoTransfer.getDestinationAccountNumber());
        TransactionDto dtoWithdraw = transactionDtoList.get(1);
        Assertions.assertEquals(withdrawAmount.toString(), dtoWithdraw.getAmount().toString());
        Assertions.assertEquals("Withdraw", dtoWithdraw.getNote());
        Assertions.assertEquals(sourceAccountNumber, dtoWithdraw.getSourceAccountNumber());
        Assertions.assertNull(dtoWithdraw.getDestinationAccountNumber());
    }
}
