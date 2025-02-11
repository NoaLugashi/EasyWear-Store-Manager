package com.example.demo.customer;

import java.math.BigDecimal;

public class ReturningCustomer extends Customer {

    public ReturningCustomer(String fullName, String idNumber, String phone) {
        super(fullName, idNumber, phone, "returning");
    }

    @Override
    public String getPurchasePlan() {
        return "Purchase plan with special discounts";
    }

    @Override
    public BigDecimal getDiscount() {
        return new BigDecimal("0.10"); // 10% discount
    }

    @Override
    public String purchase() {
        return "Performing purchase for a returning customer with special terms.";
    }
}
