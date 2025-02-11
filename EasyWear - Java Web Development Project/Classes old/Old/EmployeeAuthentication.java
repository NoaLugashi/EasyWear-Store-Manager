// package com.example.demo.employee;


// import java.util.Optional;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service
// public class EmployeeAuthentication {

//     private static final Logger logger = LoggerFactory.getLogger(EmployeeAuthentication.class);
//     private final EmployeeService employeeService;
//     private final PasswordEncoder passwordEncoder;
//     private final AuthenticationManager authenticationManager;

//     @Autowired
//     public EmployeeAuthentication(EmployeeService employeeService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
//         this.employeeService = employeeService;
//         this.passwordEncoder = passwordEncoder;
//         this.authenticationManager = authenticationManager;
//     }

//     public boolean authenticate(String employeeId, String password) {
//         // Load Employee from DB by Employee
//         Optional<Employee> employeeOptional = employeeService.getEmployeeById(employeeId);

//         if (employeeOptional.isEmpty()) {
//             // Log invalid Employee id attempt
//             logger.warn("Authentication failed: Employee '{}' not found.", employeeId);
//             throw new BadCredentialsException("Invalid Employee id or password.");
//         }

//         Employee employee = employeeOptional.get();  // Retrieve the employee from the Optional

//         // Check if password matches
//         if (!passwordEncoder.matches(password, employee.getPassword())) {
//             // Log invalid password attempt
//             logger.warn("Authentication failed: invalid password for Employee '{}'.", employeeId);
//             throw new BadCredentialsException("Invalid Employee id or password.");
//         }
        
//         // Check if password matches
//         if (passwordEncoder.matches(password, employee.getPassword())) {
//             // Create an authentication token with roles (assuming you have roles for the Employee)
//             Authentication authentication = new UsernamePasswordAuthenticationToken(employeeId, password);

//             try {
//                 // Authenticate with the AuthenticationManager
//                 Authentication authResult = authenticationManager.authenticate(authentication);

//                 // Set the authentication context (store it in the security context)
//                 SecurityContextHolder.getContext().setAuthentication(authResult);

//                 // Log successful authentication
//                 logger.info("Authentication successful for Employee '{}'.", employeeId);

//                 return true; // authentication successful


//             } catch (AuthenticationException e) {
//                 // Log invalid credentials failure
//                 logger.error("Authentication failed for Employee '{}': {}", employeeId, e.getMessage());
//                 return false;

//             }  catch (Exception e) {
//                 // Log unexpected errors
//                 logger.error("Unexpected error during authentication for Employee '{}': {}", employeeId, e.getMessage());
//                 return false;
//             }
//         }
//         return false; // authentication failed
//     }
// }
