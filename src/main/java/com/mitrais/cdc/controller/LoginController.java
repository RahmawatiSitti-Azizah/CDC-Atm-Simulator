package com.mitrais.cdc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @GetMapping("")
    public String home() {
        return "Login";
    }

    @PostMapping("login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getParameter("username"));
        System.out.println(request.getParameter("pin"));
    }
}
