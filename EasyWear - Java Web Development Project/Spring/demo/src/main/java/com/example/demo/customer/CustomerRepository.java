package com.example.demo.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;




public interface CustomerRepository extends JpaRepository <Customer, Long> {

    Optional<Customer> findBycustomerId(Long customerId);  // Fetch inventory by id

}



