package com.example.demo.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.User.UserService;
import com.example.demo.employee.EmployeeService;
import com.example.demo.log.LogService;


@Service
public class CombinedUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final LogService logService;




    public CombinedUserDetailsService(UserService userService, EmployeeService employeeService, LogService logService) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.logService = logService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        logService.addDebugLog("CombinedUserDetailsService: Attempting to load user by username: " + username);
        
        try {
            // First try to load as employee
            return employeeService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            logService.addDebugLog("CombinedUserDetailsService: Employee not found, attempting to load user by username " + username);            
        }
        // If employee not found, try to load as user
        try {
            return userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            logService.addDebugLog("CombinedUserDetailsService: User not found after employee failed\n Authentication Failuire!\n");
            
        }
    // Return null if user is not found in both Employee and User services
    return null;
}
}
