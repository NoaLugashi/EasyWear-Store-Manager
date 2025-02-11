package com.example.demo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class AuditLogUtil {


    private static final Logger logger = LoggerFactory.getLogger(AuditLogUtil.class);

    public static void setCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName(); // Get the username
            Object details = authentication.getDetails();
            Object autho = authentication.getAuthorities();
            logger.info("AuditLogUtil: Authenticated User is: " + username + " |  | User Authorities: " + autho);
            logger.info("AuditLogUtil: Authentication Details: " + details);
            MDC.put("user", username); // Put the user in MDC for use in logs
        } else {
            MDC.put("user", "anonymous"); // If no user is authenticated, set as anonymous
            
        }
    }

    public static void clear() {
        MDC.remove("user"); // Clear MDC to avoid leaking information between requests
    }
}
