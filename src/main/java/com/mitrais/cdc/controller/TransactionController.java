package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @GetMapping("")
    public String getTransactionScreen() {
        return "HistoryMenu";
    }

    @PostMapping("")
    public String processTransactionScreen(HttpServletRequest request, HttpServletResponse response, Model model) {
        int maxTransaction = Integer.parseInt(request.getParameter("max"));
        List<Transaction> transactions = new ArrayList<>();
        Account sourceAccountNumber = new Account(null, "Jane Doe", "112233", "112233");
        Account destinationAccountNumber = new Account(null, "John Doe", "112233", "112233");
        transactions.add(new Transaction(sourceAccountNumber, null, new Dollar(10.0), "W00121", "Withdraw", LocalDateTime.now()));
        transactions.add(new Transaction(sourceAccountNumber, destinationAccountNumber, new Dollar(50.0), "T00133", "", LocalDateTime.now()));
        transactions.add(new Transaction(sourceAccountNumber, destinationAccountNumber, new Dollar(100.0), "T00122", "", LocalDateTime.now()));
        model.addAttribute("transaction", transactions);
        return "HistoryList";
    }
}
