package com.mitrais.cdc.web.controller;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.dto.TransactionDto;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.TransactionAmountValidatorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/withdraw")
public class WithdrawController {
    private AccountTransactionService accountTransactionService;
    private TransactionAmountValidatorService amountValidatorService;
    private SearchAccountService searchAccountService;

    @Autowired
    public WithdrawController(AccountTransactionService accountTransactionService, TransactionAmountValidatorService amountValidatorService, SearchAccountService searchAccountService) {
        this.accountTransactionService = accountTransactionService;
        this.amountValidatorService = amountValidatorService;
        this.searchAccountService = searchAccountService;
    }

    @GetMapping("")
    public String withdrawMenu(Model model) {
        return "WithdrawMenu";
    }

    @PostMapping("")
    public String processWithdraw(HttpServletRequest request, Model model) {
        try {
            AccountDto userAccount = searchAccountService.get(request);
            try {
                Double amount = Integer.parseInt(request.getParameter("amount")) * 1.0;
                Dollar withdrawAmount = new Dollar(amount);
                amountValidatorService.validateWithdrawAmount(withdrawAmount);
                TransactionDto transaction = accountTransactionService.withdraw(userAccount, withdrawAmount);
                model.addAttribute("withdraw", transaction.getAmount());
                model.addAttribute("balance", userAccount.getBalance().toString());
                model.addAttribute("transactionDate", transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                return "WithdrawSummary";
            } catch (Exception exception) {
                model.addAttribute("errorMessage", exception.getMessage());
                return request.getParameter("other") != null ? "OtherWithdrawMenu" : "WithdrawMenu";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "Login";
        }
    }

    @GetMapping("/other")
    public String otherWithdrawMenu() {
        return "OtherWithdrawMenu";
    }
}
