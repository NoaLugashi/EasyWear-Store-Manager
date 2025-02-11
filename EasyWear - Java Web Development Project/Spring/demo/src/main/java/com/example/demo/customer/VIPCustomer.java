package com.example.demo.customer;

import java.math.BigDecimal;

public class VIPCustomer extends Customer {

    public VIPCustomer(String fullName, String idNumber, String phone) {
        super(fullName, idNumber, phone, "VIP");
    }

    @Override
    public String getPurchasePlan() {
        return "VIP purchase plan with additional services";
    }

    @Override
    public BigDecimal getDiscount() {
        return new BigDecimal("0.20"); // 20% discount
    }

    @Override
    public String purchase() {
        return "Performing purchase for a VIP customer with VIP terms.";
    }
}
