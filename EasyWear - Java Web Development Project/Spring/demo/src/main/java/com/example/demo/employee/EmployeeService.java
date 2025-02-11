package com.example.demo.employee;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.inventory.Inventory;
import com.example.demo.inventory.InventoryRepository;
import com.example.demo.log.LogService;

import jakarta.transaction.Transactional;


@Service
public class EmployeeService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final InventoryRepository inventoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    
    public EmployeeService(EmployeeRepository employeeRepository, InventoryRepository inventoryRepository, PasswordEncoder passwordEncoder, LogService logService) {
        this.employeeRepository = employeeRepository;
        this.inventoryRepository = inventoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }




    // Method to create employee
    public void addNewEmployee(Employee employee) {
        logService.addInfoLog("EmployeeService: create employee method start start");
        // Check if the employee already exists
        Optional<Employee> existingEmployee = employeeRepository.findByUsername(employee.getusername());
        logService.addInfoLog("EmployeeService: Chekcing if Username" + existingEmployee + " exists: ");
        if (existingEmployee.isPresent()) {
            throw new IllegalStateException("Username is taken");
        }
    
        // Encode the password before saving
        logService.addInfoLog("EmployeeService: Username not fonud, continue with Employee creation...");
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
        logService.addInfoLog("EmployeeService: Creation of employee ended successfully");
        
    }
    
    // Update employee information
    @Transactional
    public void updateEmployee(Long employeenumber, String username, String password, String role) {
        logService.addInfoLog("EmployeeService: Update employee information start...");
        Employee employee = employeeRepository.findByemployeenumber(employeenumber)
                .orElseThrow(() -> new IllegalStateException("Employee with id " + employeenumber + " does not exist"));
        
        logService.addInfoLog("EmployeeService: Cheking if username" + employee +  "already exists ");
        if (username != null && !username.isEmpty() && !Objects.equals(employee.getusername(), username)) {
            employee.setusername(username);
            logService.addInfoLog("EmployeeService: employee username not found, continue with Employee update ");
        }
        logService.addInfoLog("EmployeeService: Cheking if password not null ");
        if (password != null && !password.isEmpty() && !Objects.equals(employee.getPassword(), password)) {
            employee.setPassword(passwordEncoder.encode(password)); // Encoding the updated password
        }
        logService.addInfoLog("EmployeeService: Cheking if Role not null ");
        if (role != null && !role.isEmpty() && !Objects.equals(employee.getRole(), role)) {
            employee.setRole(role);
        }
        logService.addInfoLog("EmployeeService: Update employee information ended successfully");
    }
    
    // Delete an employee
    public void deleteEmployee(Long employeenumber) {
        logService.addInfoLog("EmployeeService: Delete employee start");
        boolean exists = employeeRepository.existsById(employeenumber);
        logService.addInfoLog("EmployeeService: looking for employee id: " + employeenumber);
        if (!exists) {
            logService.addInfoLog("EmployeeService: Employee with id " + employeenumber + " does not exist");
            throw new IllegalStateException("Employee with id " + employeenumber + " does not exist");
        }
        employeeRepository.deleteById(employeenumber);
        logService.addInfoLog("EmployeeService: Delete employee: " + employeenumber + "endned with success");
    }
    
    
    
        
    // This method is part of the UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logService.addDebugLog("EmployeeService: Attempting to load employee by username: " + username);
        
        // Retrieve the employee from the database
        Employee employee = employeeRepository.findByUsername(username)
        .orElseThrow(() -> {
            logService.addDebugLog("EmployeeService: User not found: " + username);
            return new UsernameNotFoundException("EmployeeService: User not found");
        });
        
        // Log the found user and their role
        logService.addDebugLog("EmployeeService: User found: " + username + " with role: " + employee.getRole()); 

        // Create the authority object for the employee's role
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + employee.getRole());

        // Return employee details with roles and password
        return org.springframework.security.core.userdetails.User
                .withUsername(employee.getusername())
                .password(employee.getPassword()) // Password should already be encoded
                .authorities(authority) // Make sure roles are properly set
                .build();
    }
    

   
    // GET method to fetch all Employees
    public List<Employee> getEmployee() {
        logService.addInfoLog("EmployeeService: GET method to fetch all Employees by findAll start");
        return employeeRepository.findAll();
    }
    
    // GET method to fetch all Employees by Usernames
    public Optional<Employee> getEmployeeByUsername(String username) {
        logService.addInfoLog("EmployeeService: GET method to fetch all Employees by findByUsername start, looking for employee" + username);
        return employeeRepository.findByUsername(username);
    }
    
    // Get an employee by their unique employee number
    public Optional<Employee> getEmployeeByNumber(Long employeenumber) {
        logService.addInfoLog("EmployeeService: GET method to fetch all Employees by findByemployeenumber start, looking for employee id: " + employeenumber);
        return employeeRepository.findById(employeenumber);
    }


    //Method to get get inventory by employee's branch
    public List<Inventory> getInventoryByEmployee(Long employeenumber) {
        logService.addInfoLog("EmployeeService: GET get inventory by employee's branch by findByemployeenumber start");
        // Find the employee by ID
        Employee employee = employeeRepository.findByemployeenumber(employeenumber)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Fetch all inventory items for the employee's branch
        logService.addInfoLog("EmployeeService: GET get inventory by employee's branch by findByemployeenumber end");
        return inventoryRepository.findBybranch(employee.getBranch());
    }
}

