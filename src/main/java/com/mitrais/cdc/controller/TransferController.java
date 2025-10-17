package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.TransactionAmountValidatorService;
import com.mitrais.cdc.util.ErrorConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transfer")
public class TransferController {
    private AccountTransactionService transactionService;
    private SearchAccountService searchAccountService;
    private TransactionAmountValidatorService amountValidatorService;
    private AccountValidatorService accountValidatorService;

    public TransferController(AccountValidatorService accountValidatorService, TransactionAmountValidatorService amountValidatorService, SearchAccountService searchAccountService, AccountTransactionService transactionService) {
        this.accountValidatorService = accountValidatorService;
        this.amountValidatorService = amountValidatorService;
        this.searchAccountService = searchAccountService;
        this.transactionService = transactionService;
    }

    @GetMapping("")
    public String getTransferMenu() {
        return "TransferMenu";
    }

    @PostMapping("")
    public String processTransfer(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Account sourceAccount = searchAccountService.get(request);

            String destinationAccountNumber = request.getParameter("destAccount");
            accountValidatorService.validateAccountNumber(destinationAccountNumber, ErrorConstant.INVALID_ACCOUNT);
            Account destinationAccount = searchAccountService.get(destinationAccountNumber);

            Double amount = Double.parseDouble(request.getParameter("amount"));
            Money transferAmount = new Dollar(amount);
            amountValidatorService.validateTransferAmount(transferAmount);

            transactionService.transfer(sourceAccount, destinationAccount, transferAmount, "");
            model.addAttribute("amount", transferAmount);
            model.addAttribute("balance", sourceAccount.getStringBalance());
            return "TransferSummary";
        } catch (Exception e) {

            return "Login";
        }
    }
}
