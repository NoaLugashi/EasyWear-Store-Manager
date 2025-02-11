package com.example.demo.customer;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @SequenceGenerator(
        name = "customer_Sequence",
        sequenceName = "customer_Sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "customer_Sequence"
    )
    @Column(name = "customerId", unique = true)
    private Long customerId;
    private String fullName;

    @Column(name = "idNumber", unique = true, nullable = false)
    private String idNumber;

    private String phone;
    private String customerType; // new, returning, VIP


    // Empty Constructor
    public Customer() {
    }

    // Full Constructor
    public Customer(Long customerId, String fullName, String idNumber, String phone, String customerType) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.phone = phone;
        this.customerType = customerType;
    }

    // Constructor without id
    public Customer(String fullName, String idNumber, String phone, String customerType) {
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.phone = phone;
        this.customerType = customerType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerType() {
        return customerType;
    }

  public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getCustomerId() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", phone='" + getPhone() + "'" +
            ", customerType='" + getCustomerType() + "'" +
            "}";
    }


    // Default purchase plan for generic customer
    public String getPurchasePlan() {
        return "Standard purchase plan";
    }

    // Default discount for generic customer
    public BigDecimal getDiscount() {
        return BigDecimal.ZERO; // No discount by default
    }

    // Default purchase method for generic customer
    public String purchase() {
        return "Performing purchase for a general customer.";
    }



}
