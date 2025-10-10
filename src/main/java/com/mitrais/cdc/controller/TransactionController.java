package com.mitrais.cdc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransactionController {

    @GetMapping("/transaction")
    public String getTransactionScreen() {
        return "HistoryMenu";
    }

    @PostMapping("/transaction")
    public String processTransactionScreen(HttpServletRequest request, HttpServletResponse response) {

        return "TransferSummary";
    }
}
