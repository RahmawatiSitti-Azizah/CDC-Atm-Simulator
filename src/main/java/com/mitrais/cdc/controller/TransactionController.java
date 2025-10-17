package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("")
    public String getTransactionScreen() {
        return "HistoryMenu";
    }

    @PostMapping("")
    public String processTransactionScreen(HttpServletRequest request, HttpServletResponse response, Model model) {
        int maxTransaction = Integer.parseInt(request.getParameter("max"));
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        List<Transaction> transactions = transactionService.getTransactionHistoryAccount(account.getAccountNumber(), maxTransaction);
        model.addAttribute("transaction", transactions);
        return "HistoryList";
    }
}
