package com.example.demo.customer;

import java.math.BigDecimal;

public class NewCustomer extends Customer {

    public NewCustomer(String fullName, String idNumber, String phone) {
        super(fullName, idNumber, phone, "new");
    }

    @Override
    public String getPurchasePlan() {
        return "Standard purchase plan";
    }

    @Override
    public BigDecimal getDiscount() {
        return new BigDecimal("0.05"); // 5% discount
    }

    @Override
    public String purchase() {
        return "Performing purchase for a new customer with standard terms.";
    }
}
