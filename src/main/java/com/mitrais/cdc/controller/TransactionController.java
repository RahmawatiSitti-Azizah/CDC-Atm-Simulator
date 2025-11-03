package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.dto.TransactionDto;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.TransactionService;
import com.mitrais.cdc.util.ErrorConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    private TransactionService transactionService;
    private SearchAccountService searchAccountService;

    @Autowired
    public TransactionController(SearchAccountService searchAccountService, TransactionService transactionService) {
        this.searchAccountService = searchAccountService;
        this.transactionService = transactionService;
    }

    @GetMapping("")
    public String historyMenu() {
        return "HistoryMenu";
    }

    @PostMapping("")
    public String processTransaction(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            AccountDto account = searchAccountService.get(request);
            try {
                String inputMax = request.getParameter("max");
                int maxTransaction = Integer.parseInt(inputMax != null ? inputMax : "0");
                List<TransactionDto> transactions = transactionService.getTransactionHistoryAccount(account.getAccountNumber(), maxTransaction);
                model.addAttribute("transaction", transactions);
                return "HistoryList";
            } catch (Exception exception) {
                model.addAttribute("errorMessage", ErrorConstant.INVALID_MAX_TRANSACTION_SHOWN);
                return "HistoryMenu";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "Login";
        }
    }
}
