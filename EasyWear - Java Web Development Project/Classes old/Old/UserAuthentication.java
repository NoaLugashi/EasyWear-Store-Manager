// package com.example.demo.user;


// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service
// public class UserAuthentication {

//     private static final Logger logger = LoggerFactory.getLogger(UserAuthentication.class);
//     private final UserService userService;
//     private final BCryptPasswordEncoder passwordEncoder;
//     private final AuthenticationManager authenticationManager;

//     @Autowired
//     public UserAuthentication(UserService userService, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
//         this.userService = userService;
//         this.passwordEncoder = passwordEncoder;
//         this.authenticationManager = authenticationManager;
//     }

//     public boolean authenticate(String username, String password) {
//         // Load user from DB by username
//         User user = userService.getUserByUsername(username);

//         if (user == null) {
//             // Log invalid username attempt
//             logger.warn("Authentication failed: user '{}' not found.", username);
//             throw new BadCredentialsException("Invalid username or password.");
//         }

//         // Check if password matches
//         if (!passwordEncoder.matches(password, user.getPassword())) {
//             // Log invalid password attempt
//             logger.warn("Authentication failed: invalid password for user '{}'.", username);
//             throw new BadCredentialsException("Invalid username or password.");
//         }
        
//         // Check if password matches
//         if (passwordEncoder.matches(password, user.getPassword())) {
//             // Create an authentication token with roles (assuming you have roles for the user)
//             Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

//             try {
//                 // Authenticate with the AuthenticationManager
//                 Authentication authResult = authenticationManager.authenticate(authentication);

//                 // Set the authentication context (store it in the security context)
//                 SecurityContextHolder.getContext().setAuthentication(authResult);

//                 // Log successful authentication
//                 logger.info("Authentication successful for user '{}'.", username);

//                 return true; // authentication successful


//             } catch (BadCredentialsException  e) {
//                 // Log the failure to authenticate
//                 logger.error("Authentication failed for user '{}': {}", username, e.getMessage());
//                 // Handle failure to authenticate (e.g., incorrect credentials)
//                 return false; // authentication failed
//             }
//             catch (Exception e) {
//                 // Log any other unexpected errors
//                 logger.error("Unexpected error during authentication for user '{}': {}", username, e.getMessage());
//                 return false; // authentication failed
//             }
//         }
//         return false; // authentication failed
//     }
// }
