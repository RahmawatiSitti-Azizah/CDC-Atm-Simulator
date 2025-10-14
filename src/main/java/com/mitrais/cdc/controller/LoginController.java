package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.util.ErrorConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.security.auth.login.CredentialNotFoundException;

@Controller
public class LoginController {
    private AccountValidatorService validatorService;
    private SearchAccountService searchAccountService;

    @Autowired
    public LoginController(SearchAccountService searchAccountService, AccountValidatorService validatorService) {
        this.searchAccountService = searchAccountService;
        this.validatorService = validatorService;
    }

    @GetMapping("")
    public String home(Model model) {
        return "Login";
    }

    @PostMapping("login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession();
        if (isSessionValid(session)) {
            return "TransactionMenu";
        }
        String account = request.getParameter("account");
        String pin = request.getParameter("pin");
        try {
            session.setAttribute("account", getAccount(account, pin));
            return "TransactionMenu";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "Login";
        }
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("account");
        return "Login";
    }

    private Account getAccount(String account, String pin) throws Exception {
        if (account == null && pin == null) {
            throw new CredentialNotFoundException(ErrorConstant.INVALID_ACCOUNT_PASSWORD);
        }
        validatorService.validateAccountNumber(account, ErrorConstant.ACCOUNT_NUMBER_SHOULD_ONLY_CONTAINS_NUMBERS);
        validatorService.validatePin(pin);
        return searchAccountService.get(account, pin);
    }

    private static boolean isSessionValid(HttpSession session) {
        return session.getAttribute("account") != null;
    }
}
