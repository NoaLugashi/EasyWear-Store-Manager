package com.example.demo.sales;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @SequenceGenerator(
        name = "sales_Sequence",
        sequenceName = "sales_Sequence",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    @Column(name = "transactionId", unique = true, nullable = false)
    private Long transactionId;

    private String customerName;
	private String customerType;
	private String productName;
    private String productCategory;
    private int quantity;
    private BigDecimal price;
    private String branch;


    // Empty constructor
    public Sales() {
    }

    // Constructor with all fields
    public Sales(Long transactionId, String customerName, String customerType, String productName, String productCategory, Integer quantity, BigDecimal price,  String branch) {
        this.transactionId = transactionId;
		this.customerName = customerName;
		this.customerType = customerType;
        this.productName = productName;
		this.productCategory = productCategory;
        this.quantity = quantity;
		this.price = price;
        this.branch = branch;
    }    

    // Constructor without ID
    public Sales(String customerName, String customerType, String productName, String productCategory, Integer quantity, BigDecimal price,  String branch) {
		this.customerName = customerName;
		this.customerType = customerType;
        this.productName = productName;
		this.productCategory = productCategory;
        this.quantity = quantity;
		this.price = price;
        this.branch = branch;
    }    


    // Getters
    public Long getId() {
        return transactionId;
    }
	
    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerType() {
        return customerType;
    }
	
    public String getProductName() {
        return productName;
    }
	
    public String getProductCategory() {
        return productCategory;
    }
	
    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getBranch() {
        return branch;
    }


    // Setters
    public void setId(Long transactionId) {
        this.transactionId = transactionId;
    }
	
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
	
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
	
    public void setProductName(String productName) {
        this.productName = productName;
    }
	
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }


    @Override
    public String toString() {
        return "{" +
            " Sale id='" + getId() + "'" +
            " Customer Name='" + getCustomerName() + "'" +
            ", Customer Type='" + getCustomerType() + "'" +
            ", Product Name='" + getProductName() + "'" +
            ", ProductCategory='" + getProductCategory() + "'" +
            ", Quantity='" + getQuantity() + "'" +
            ", Price='" + getPrice() + "'" +
            ", Branch='" + getBranch() + "'" +
            "}";
    }
}

