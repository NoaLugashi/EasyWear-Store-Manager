package com.example.demo.authentication;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.log.LogService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private LogService logService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        // Get authorities of the principal (this returns a Collection of GrantedAuthority)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Debugging - Print out authorities to check
        logService.addDebugLog("CustomAuthenticationSuccessHandler: Checking Authority...");

        // Check if the user has 'ROLE_Admin'
        if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_Admin"))) {
            logService.addDebugLog("CustomAuthenticationSuccessHandler: Check if the user has 'ROLE_Admin' " + authorities);
            response.sendRedirect("/AdminPanel.html");  // Redirect to Admin dashboard

        } else if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_Employee"))) {
            logService.addDebugLog("CustomAuthenticationSuccessHandler: Check if the user has 'ROLE_Employee' " + authorities);
            response.sendRedirect("/EmployeePanel.html");  // Redirect to Employee dashboard

        } else {
            response.sendRedirect("/login-failure.html");  // Fallback
            logService.addDebugLog("CustomAuthenticationSuccessHandler: login failed - redirecting to login-failure page");
        }
    }
}

