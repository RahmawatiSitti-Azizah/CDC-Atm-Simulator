package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
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
    public String getWithdrawMenu(Model model) {
        return "WithdrawMenu";
    }

    @PostMapping("")
    public String WithdrawMenu(HttpServletRequest request, Model model) {
        try {
            Account userAccount = searchAccountService.get(request);
            Double amount = Integer.parseInt(request.getParameter("amount")) * 1.0;
            Dollar withdrawAmount = new Dollar(amount);
            try {
                amountValidatorService.validateWithdrawAmount(withdrawAmount);
                accountTransactionService.withdraw(userAccount, withdrawAmount);
                model.addAttribute("withdraw", amount);
                model.addAttribute("balance", userAccount.getStringBalance());
                return "WithdrawSummary";
            } catch (Exception exception) {
                model.addAttribute("errorMessage", exception.getMessage());
                return "WithdrawMenu";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "Login";
        }
    }

    @GetMapping("/other")
    public String getOtherWithdrawMenu() {
        return "OtherWithdrawMenu";
    }
}
