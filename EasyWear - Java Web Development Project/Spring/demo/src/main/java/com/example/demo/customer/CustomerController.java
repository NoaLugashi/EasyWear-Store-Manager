package com.example.demo.customer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.log.AuditLogUtil;
import com.example.demo.log.LogService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final LogService logService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    // @Autowired
    public CustomerController(CustomerService customerService, LogService logService) {
        this.customerService = customerService;
        this.logService = logService;
    }

    // GET all Customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        logService.addWarnLog("CustomerController: Accssesing @GetMapping(getAllCustomers) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getAllCustomers"); // Logs action with user info
        AuditLogUtil.clear();
        return customerService.getCustomers();
    }


    // POST to create a new Customer
    @PostMapping
    public Customer addNewCustomer(@RequestBody Customer Customer) {
        logService.addWarnLog("CustomerController: Accssesing @PostMapping(addNewCustomer) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: addNewCustomer"); // Logs action with user info
        AuditLogUtil.clear();
        customerService.addNewCustomer(Customer);
        return Customer;
    }

    // DELETE a Customer
    @DeleteMapping(path = "{customerId}")
    public void deleteCustomer(@PathVariable ("customerId") Long customerId) {
        logService.addWarnLog("CustomerController: Accssesing @DeleteMapping(DeleteCustomer) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: deleteCustomer"); // Logs action with user info
        AuditLogUtil.clear();
        customerService.deleteCustomer(customerId);
    }


}
