package com.example.demo.inventory;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    @Column(name = "inventory_id", unique = true, nullable = false)
    private Long inventoryId;

    private String productName;
    private int quantity;
    private BigDecimal price;
    private String productCategory;
    private String branch;


    // Empty constructor
    public Inventory() {
    }

    // Constructor with all fields
    public Inventory(Long inventoryId, String productName, Integer quantity, String productCategory, String branch) {
        this.inventoryId = inventoryId;
        this.productName = productName;
        this.quantity = quantity;
        this.productCategory = productCategory;
        this.branch = branch;
    }    

    // Constructor without ID
    public Inventory(String productName, int quantity, BigDecimal price, String productCategory, String branch) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.productCategory = productCategory;
        this.branch = branch;
    }

    // Getters
    public Long getInventoryId() {
        return inventoryId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getProductCategory(){
        return productCategory;
    }

    public String getBranch() {
        return branch;
    }


    // Setters
    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setProductCategory(String productCategory){
        this.productCategory = productCategory;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }


    @Override
    public String toString() {
        return "{" +
            " Inventory id='" + getInventoryId() + "'" +
            ", Product Name='" + getProductName() + "'" +
            ", Quantity='" + getQuantity() + "'" +
            ", Price='" + getPrice() + "'" +
            ", ProductCategory='" + getProductCategory() + "'" +
            ", Branch='" + getBranch() + "'" +
            "}";
    }
}

