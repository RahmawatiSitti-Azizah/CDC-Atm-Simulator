package com.mitrais.cdc.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/withdraw")
public class WithdrawController {

    @GetMapping("")
    public String getWithdrawMenu(Model model) {
        return "WithdrawMenu";
    }

    @PostMapping("")
    public String WithdrawMenu(HttpServletRequest request, Model model) {
        int amount = Integer.parseInt(request.getParameter("amount"));
        model.addAttribute("withdraw", amount);
        model.addAttribute("balance", "$350");
        return "WithdrawSummary";
    }
}
