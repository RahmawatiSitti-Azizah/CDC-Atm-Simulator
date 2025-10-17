package com.mitrais.cdc.controller;

import com.mitrais.cdc.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private LoginService service;

    @Autowired
    public LoginController(LoginService service) {
        this.service = service;
    }

    @GetMapping("")
    public String home(Model model) {
        return "Login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession();
        if (isSessionValid(session)) {
            return "TransactionMenu";
        }
        try {
            session.setAttribute("account", service.login(request.getParameter("account"), request.getParameter("pin")));
            return "TransactionMenu";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "Login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("account");
        return "Login";
    }

    private static boolean isSessionValid(HttpSession session) {
        return session.getAttribute("account") != null;
    }
}
