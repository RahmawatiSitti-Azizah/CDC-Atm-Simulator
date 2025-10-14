package com.mitrais.cdc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transfer")
public class TransferController {
    @GetMapping("")
    public String getTransferMenu() {
        return "TransferMenu";
    }

    @PostMapping("")
    public String processTransfer(HttpServletRequest request, HttpServletResponse response) {
        return "TransferSummary";
    }
}
