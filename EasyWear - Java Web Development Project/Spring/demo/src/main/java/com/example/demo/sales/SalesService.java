package com.example.demo.sales;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRepository;
import com.example.demo.inventory.Inventory;
import com.example.demo.inventory.InventoryRepository;
import com.example.demo.log.LogService;


@Service
public class SalesService {


    private final SalesRepository salesRepository;
    private final LogService logService;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;

    public SalesService(SalesRepository salesRepository, LogService logService, CustomerRepository customerRepository, InventoryRepository inventoryRepository) {
        this.salesRepository = salesRepository;
        this.logService = logService;
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // GET all Sales
    public List<Sales> getSales() {
        logService.addInfoLog("SalesService: GET method to fetch Sale Transactions start");
        return salesRepository.findAll();
    }

    // GET Sales by branch
    public List<Sales> getSalesBybranch(String branch) {
        logService.addInfoLog("SalesService: GET method to fetch Sale Transactions By branch start, looking for branch: " + branch);
        return salesRepository.findBybranch(branch);
    }

    // GET Sales by Product Name
    public List<Sales> getSalesByProductName(String productName) {
        logService.addInfoLog("SalesService: GET method to fetch Sale Transactions By productName start, looking for producName: " + productName);
        return salesRepository.findByproductName(productName);
    }

    // GET Sales by Product Category
    public List<Sales> getSalesByProductCategory(String productCategory) {
        logService.addInfoLog("SalesService: GET method to fetch Sale Transactions By productCategory start, looking for productCategory" + productCategory);
        return salesRepository.findByproductCategory(productCategory);
    }

    // POST method to add a new Sale Transaction
    public void addNewSales(Sales Sale) {
        logService.addInfoLog("SalesService: POST method to add a new Sales start");
        Optional<Sales> SalesOptional = salesRepository.findBytransactionId(Sale.getId());
        if (SalesOptional.isPresent()) {
            throw new IllegalStateException("Sale Transaction ID is taken");
        }
        salesRepository.save(Sale);
        logService.addInfoLog("SalesService: POST method to add a new Sale ended with success");
    }


    // Delete method to delete Sale Transaction
    void deleteSales(Long id) {
        logService.addInfoLog("SalesService: DELETE method to delete a Sale Transaction start, looking for id: " + id);
        boolean exists = salesRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Sales with id " + id + " does not exist");
        }
        salesRepository.deleteById(id);
        logService.addInfoLog("SalesService: DELETE method to delete a Sale Transaction ended with success!");
    }



    // POST method to handle a purchase
    public void buy(Long customerId, String productName, int quantity) {
        logService.addInfoLog("SalesService: POST method to process purchase start");
        logService.addInfoLog("SalesService: Customer id is: " + customerId + "Product Name is: " + productName + "Quantity is: " + quantity);

        // Fetch the customer 
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("Customer with id " + customerId + " does not exist"));

        // Fetch the inventory item
        Inventory inventory = inventoryRepository.findByProductName(productName)
                .orElseThrow(() -> new IllegalStateException("Inventory item with product name " + productName + " does not exist"));

        // Check if enough stock is available
        if (inventory.getQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + productName);
        }

        // Calculate price based on customer type
        BigDecimal salePrice = inventory.getPrice();
        BigDecimal discount = calculateDiscount(customer);
        BigDecimal finalPrice = salePrice.subtract(salePrice.multiply(discount));

        // Update inventory
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);

        // Create and save the sale entry
        Sales sale = new Sales(customer.getFullName(), customer.getCustomerType(), productName, inventory.getProductCategory(),
                quantity, finalPrice, inventory.getBranch());
        salesRepository.save(sale);

        logService.addInfoLog("SalesService: POST method to process purchase ended successfully");
    }

    // Helper method to calculate discount based on customer type
    private BigDecimal calculateDiscount(Customer customer) {
        logService.addInfoLog("SalesService: Calculating Discount for customer: " + customer);
        BigDecimal discount = BigDecimal.ZERO;
        if (null != customer.getCustomerType()) switch (customer.getCustomerType()) {
            case "New" -> discount = new BigDecimal("0.05"); // 5% discount
            case "Returning" -> discount = new BigDecimal("0.10"); // 10% discount
            case "VIP" -> discount = new BigDecimal("0.20"); // 20% discount
            default -> {
            }
        }
        return discount;
    }
}
