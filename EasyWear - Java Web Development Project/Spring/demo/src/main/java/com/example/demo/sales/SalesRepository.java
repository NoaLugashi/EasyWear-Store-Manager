package com.example.demo.sales;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface SalesRepository extends JpaRepository<Sales, Long> {

    Optional<Sales> findBytransactionId(Long transactionId);  // Fetch Sales by id

    List<Sales> findBycustomerName(String customerName); // Fetch Sales by Customername

    List<Sales> findByproductName(String productName); // Fetch Sales by Productname

    List<Sales> findByproductCategory(String productCategory); // Fetch Sales by Productcategory

    List<Sales> findBybranch(String branch); // Fetch Sales by Productbranch

}
