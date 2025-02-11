package com.example.demo.customer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.log.LogService;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final LogService logService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, LogService logService) {
        this.customerRepository = customerRepository;
        this.logService = logService;
    }

    // GET method to fetch all customers
    public List<Customer> getCustomers() {
        logService.addWarnLog("CustomerService: GET method to fetch all Customers by findAll start ");
        return customerRepository.findAll();
    }

    // POST method to add a new customer
    public void addNewCustomer(Customer Customer) {
        logService.addWarnLog("CustomerService: POST method to add a new customer start ");
        Optional<Customer> CustomerOptional = customerRepository.findBycustomerId(Customer.getCustomerId());
        if (CustomerOptional.isPresent()) {
            throw new IllegalStateException("Customer with this ID number already exists");
        }
        customerRepository.save(Customer);
        logService.addWarnLog("CustomerService: POST method to add a new customer ended with success ");
    }

    // DELETE method to delete a customer
    public void deleteCustomer(Long customerId) {
        logService.addWarnLog("CustomerService: DELETE method to delete a customer start ");
        boolean exists = customerRepository.existsById(customerId);
        if (!exists) {
            throw new IllegalStateException("Customer with id " + customerId + " does not exist");
        }
        customerRepository.deleteById(customerId);
        logService.addWarnLog("CustomerService: DELETE method to delete a customer ended with success ");
    }

    // PUT method to update customer details (name, phone, etc.)
    @Transactional
    public void updateCustomer(Long customerId, String fullName, String phone, String customerType) {
        logService.addWarnLog("CustomerService: PUT method to update customer details start ");
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("Customer with id " + customerId + " does not exist"));

        if (fullName != null && !fullName.isEmpty() && !Objects.equals(customer.getFullName(), fullName)) {
            customer.setFullName(fullName);
        }
        if (phone != null && !phone.isEmpty() && !Objects.equals(customer.getPhone(), phone)) {
            customer.setPhone(phone);
        }
        if (customerType != null && !customerType.isEmpty() && !Objects.equals(customer.getCustomerType(), customerType)) {
            customer.setCustomerType(customerType);
        }
        logService.addWarnLog("CustomerService: PUT method to update customer details ended with success ");
    }



}
