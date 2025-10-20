package com.mitrais.cdc.filter;

import com.mitrais.cdc.service.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private LoginService service;
    private static final String[] WHITELIST_URL = {
            "/",
            "/WEB-INF/jsp/Login.jsp"};

    public AuthenticationFilter(LoginService service) {
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        if (session.getAttribute("account") != null)
            filterChain.doFilter(request, response);
        else {
            if (req.getRequestURI().equals("/login")) {
                filterChain.doFilter(request, response);
            } else {
                HttpServletResponse resp = (HttpServletResponse) response;
                resp.sendRedirect("");
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return requestURI.endsWith(".css") || requestURI.endsWith(".js") || requestURI.contains("h2-console") || Arrays.stream(WHITELIST_URL).anyMatch(s -> s.equals(requestURI));
    }
}
