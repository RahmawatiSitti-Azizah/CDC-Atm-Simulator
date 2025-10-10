package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @GetMapping("")
    public String home(Model model) {
        return "Login";
    }

    @PostMapping("login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (isHasAccount(session) || isAccountParameterCorrect(request)) {
            session.setAttribute("account", new Account(new Dollar(10.0), "Jane Doe", "112233", ""));
            return "TransactionMenu";
        } else {
            return "Login";
        }
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("account");
        return "Login";
    }

    private static boolean isAccountParameterCorrect(HttpServletRequest request) {
        return request.getParameter("account").equals("112233") && request.getParameter("pin").equals("112233");
    }

    private static boolean isHasAccount(HttpSession session) {
        return session.getAttribute("account") != null;
    }
}
