package com.example.demo.inventory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.log.LogService;

import jakarta.transaction.Transactional;

@Service
public class InventoryService {


    private final InventoryRepository inventoryRepository;
    private final LogService logService;

    public InventoryService(InventoryRepository inventoryRepository, LogService logService) {
        this.inventoryRepository = inventoryRepository;
        this.logService = logService;
    }

    // GET all Inventory
    public List<Inventory> getInventory() {
        logService.addInfoLog("InventoryService: GET method to fetch all Inventory start");
        return inventoryRepository.findAll();
    }

    // GET Inventory by branch
    public List<Inventory> getInventoryBybranch(String branch) {
        logService.addInfoLog("InventoryService: GET method to fetch Inventory By branch start, looking for branch: " + branch);
        return inventoryRepository.findBybranch(branch);
    }

    // GET Inventory by Product Name
    public Optional<Inventory> getInventoryByProductName(String productName) {
        logService.addInfoLog("InventoryService: GET method to fetch Inventory By productName start, looking for Product Name: " + productName);
        return inventoryRepository.findByProductName(productName);
    }

    // GET Inventory by Product Category
    public List<Inventory> getInventoryByProductCategory(String productCategory) {
        logService.addInfoLog("InventoryService: GET method to fetch Inventory By productName start");
        logService.addInfoLog("InventoryService: GET method to fetch Inventory By productName start, looking for Product Category: " + productCategory);
        return inventoryRepository.findByproductCategory(productCategory);
    }

    // POST method to add a new Inventory
    public void addNewInventory(Inventory Inventory) {
        logService.addInfoLog("InventoryService: POST method to add a new Inventory start");
        Optional<Inventory> InventoryOptional = inventoryRepository.findByInventoryId(Inventory.getInventoryId());
        if (InventoryOptional.isPresent()) {
            throw new IllegalStateException("Inventoryname is taken");
        }
        logService.addInfoLog("InventoryService: Inventory requested is: " + Inventory);
        inventoryRepository.save(Inventory);
        logService.addInfoLog("InventoryService: POST method to add a new Inventory ended with success");
    }

    // PUT method to update Inventory
    @Transactional
    public void updateInventory(Long inventoryId, String productName, int quantity, BigDecimal price, String productCategory, String branch) {
        logService.addInfoLog("InventoryService: PUT method to update Inventory start");
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new IllegalStateException("Inventory with id " + inventoryId + " does not exist"));
		inventory.setProductName(productName);
		inventory.setQuantity(quantity);
		inventory.setPrice(price);
		inventory.setProductCategory(productCategory);
		inventory.setBranch(branch);
        logService.addInfoLog("InventoryService: PUT method to update Inventory end");
    }

    // Delete method to delete Inventory
    void deleteInventory(Long inventoryId) {
        logService.addInfoLog("InventoryService: DELETE method to delete a Inventory Item start, looking for inventory id: " + inventoryId);
        boolean exists = inventoryRepository.existsById(inventoryId);
        if (!exists) {
            throw new IllegalStateException("Inventory with id " + inventoryId + " does not exist");
        }
        logService.addInfoLog("InventoryService: inventory id found, Deleting inventory id: " + inventoryId);
        inventoryRepository.deleteById(inventoryId);
        logService.addInfoLog("InventoryService: DELETE method to delete a Inventory Item ended with success");
    }

}
