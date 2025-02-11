package com.example.demo.employee;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.log.AuditLogUtil;
import com.example.demo.log.LogService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final LogService logService;
    private final EmployeeRepository employeeRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class); 

    public EmployeeController(EmployeeService employeeService, LogService logService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.logService = logService;
        this.employeeRepository = employeeRepository;
    }

    // GET all Employees
    @GetMapping
    public List <Employee> getEmployee() {
        logService.addWarnLog("EmployeeController: Accssesing @GetMapping(getEmployee) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getEmployee"); // Logs action with user info
        AuditLogUtil.clear();
        return employeeService.getEmployee();
    }
    
    // Endpoint to create an employee
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        employeeService.addNewEmployee(employee);
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: addNewEmployee"); // Logs action with user info
        AuditLogUtil.clear();
        logService.addWarnLog("EmployeeController: Accssesing @PostMapping(createEmployee) ");
        return employee;
    }

    // Update an employee
    @PutMapping(path = "{employeenumber}")
    public Employee updateEmployee(
        @PathVariable("employeenumber") Long employeenumber,
        @RequestBody Employee employee) {
        
        logService.addWarnLog("EmployeeController: Accessing @PutMapping(updateEmployee)");
        // Fetch the existing employee from the database by employeenumber
        Employee existingEmployee = employeeService.getEmployeeByNumber(employeenumber)
        .orElseThrow(() -> new RuntimeException("Employee not found"));
    
        // Update the fields only if they are provided (non-null)
        if (employee.getusername() != null) {
            existingEmployee.setusername(employee.getusername());
        }
        if (employee.getFirstName() != null) {
            existingEmployee.setFirstName(employee.getFirstName());
        }
        if (employee.getLastName() != null) {
            existingEmployee.setLastName(employee.getLastName());
        }
        if (employee.getPassword() != null) {
            existingEmployee.setPassword(employee.getPassword());
        }
        if (employee.getEmail() != null) {
            existingEmployee.setEmail(employee.getEmail());
        }
        if (employee.getRole() != null) {
            existingEmployee.setRole(employee.getRole());
        }
        if (employee.getPhone() != null) {
            existingEmployee.setPhone(employee.getPhone());
        }
        if (employee.getBranch() != null) {
            existingEmployee.setBranch(employee.getBranch());
        }
        if (employee.getDob() != null) {
            existingEmployee.setDob(employee.getDob());
        }
        if (employee.getAge() != null) {
            existingEmployee.setAge(employee.getAge());
        }
    
        // Save the updated employee to the database
        employeeRepository.save(existingEmployee);
    
        // Log the action
        logService.addWarnLog("EmployeeController: Accessing @PutMapping(updateEmployee)");
    
        // Return the updated employee
        return existingEmployee;
    }
    

    // Delete an employee by their unique employeenumber
    @DeleteMapping(path = "{employeenumber}")
    public void deleteEmployee(@PathVariable ("employeenumber") Long employeenumber) {
        logService.addWarnLog("EmployeeController: Accssesing @DeleteMapping(deleteEmployee) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: deleteEmployee"); // Logs action with user info
        AuditLogUtil.clear();
        employeeService.deleteEmployee(employeenumber);
        
    }



    @GetMapping("/{employeenumber}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String employeenumber) {
        logService.addWarnLog("EmployeeController: Accssesing @GetMapping(\"/{employeenumber}\") ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getEmployeeById"); // Logs action with user info
        AuditLogUtil.clear();
        try {
            Long employeeNumber = Long.valueOf(employeenumber);  // Convert to Long
            Optional<Employee> employee = employeeService.getEmployeeByNumber(employeeNumber);
            return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);  // Return bad request if conversion fails
        }
    }


    @PostMapping("/login")
    public String authenticateUser(@RequestParam String username, @RequestParam String password) {
        logService.addWarnLog("EmployeeController: Accssesing @PostMapping(\"/login\") ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: authenticateUser login"); // Logs action with user info
        AuditLogUtil.clear();        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        if (isAuthenticated) {
            return "Authentication successful!";
        } else {
            return "Authentication failed!";
        }
    }
    

}
