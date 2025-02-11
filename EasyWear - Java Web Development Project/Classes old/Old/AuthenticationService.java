package com.example.demo.authentication;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.log.LogService;
import ch.qos.logback.classic.Logger;

@Service
public class AuthenticationService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthenticationService.class);
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CombinedUserDetailsService combinedUserDetailsService;
    private final LogService logService;



    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 CombinedUserDetailsService combinedUserDetailsService,
                                 LogService logService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.combinedUserDetailsService = combinedUserDetailsService;
        this.logService = logService;
    }

    // Generic authentication method for both User and Employee
    public Boolean authenticate(String username, String password, boolean isEmployee) {
        logger.info("Authentication attempt for user: '{}', isEmployee: {}", username, isEmployee);
        logService.addDebugLog("AuthenticationService1: Authentication attempt for user: " + username);  // Log to in-memory service


        UserDetails userDetails;
        try {
            // Try loading UserDetails via CombinedUserDetailsService
            userDetails = combinedUserDetailsService.loadUserByUsername(username);
            logService.addDebugLog("AuthenticationService2: Authentication attempt for user: " + username);  // Log to in-memory service
        } catch (UsernameNotFoundException ex) {
            logger.warn("Authentication failed: '{}' not found in system.", username);
            logger.debug("Username '{}' not found in system.", username);
            logger.info("Username '{}' not found in system.", username);
            logService.addDebugLog("AuthenticationService3: Authentication failed: '" + username + "' not found."); // Log to in-memory service
            return null;
            
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            logger.warn("Authentication failed: Invalid password for user '{}'. Provided: {}, Stored: {}", username, password, userDetails.getPassword());
            logger.debug("Username '{}' not found in system.", username);
            logger.info("Username '{}' not found in system.", username);
            logService.addDebugLog("AuthenticationService4: Authentication failed: Invalid password for user '" + username + "'."); // Log to in-memory service
            return null;
        }


        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        logService.addDebugLog("AuthenticationService5: Authentication attempt for user: " + username);  // Log to in-memory service

        try {

            logger.info("Attempting to authenticate with AuthenticationManager.");
            logService.addDebugLog("AuthenticationService6: Attempting to authenticate with AuthenticationManager."); // Log to in-memory service

            // Authenticate with the AuthenticationManager
            Authentication authResult = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authResult);


            logger.info("Authentication successful for '{}'.", username);
            logService.addDebugLog("AuthenticationService7: Authentication successful for '" + username + "'.");

            return true; // authentication successful

        } catch (BadCredentialsException e) {
            logger.error("Authentication failed (Password) for '{}': {}", username, e.getMessage());
            logService.addDebugLog("AuthenticationService8: Authentication failed for '" + username + "': " + e.getMessage()); // Log to in-memory service
            return false;

        } catch (UsernameNotFoundException ex) {
            logger.error("Username '{}' not found in system.", username);
            logService.addDebugLog("AuthenticationService9: Authentication failed for '" + username + "': " + ex.getMessage()); // Log to in-memory service
            return false;
        }
         catch (Exception e) {
            logger.error("Unexpected error during authentication for '{}': {}", username, e.getMessage());
            logService.addDebugLog("AuthenticationService10: Unexpected error during authentication for '" + username + "': " + e.getMessage()); // Log to in-memory service
            return false;
        }
    }
}
