package com.example.demo.inventory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.log.AuditLogUtil;
import com.example.demo.log.LogService;

@RestController
@RequestMapping(path = "api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final LogService logService;
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class); 

    public InventoryController(InventoryService inventoryService, LogService logService) {
        this.inventoryService = inventoryService;
        this.logService = logService;
    }

    // GET all Inventory
    @GetMapping
    public List <Inventory> getInventory() {
        logService.addWarnLog("InventoryController: Accssesing @GetMapping(getInventory) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getInventory"); // Logs action with user info
        AuditLogUtil.clear();
        return inventoryService.getInventory();
    }

    //GET inventory by branch
    @GetMapping("/branch/{branch}")
    public List<Inventory> getInventory(@PathVariable ("branch") String branch) {
        logService.addWarnLog("InventoryController: Accssesing @GetMapping(branch) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getInventoryByBranch"); // Logs action with user info
        AuditLogUtil.clear();
        return inventoryService.getInventoryBybranch(branch);
    }
    
    //GET inventory by productName
    @GetMapping("/productName/{productName}")
    public Optional<Inventory> getInventoryByProductName(@PathVariable ("productName") String productName) {
        logService.addWarnLog("InventoryController: Accssesing @GetMapping(productName) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getInventoryByProductName"); // Logs action with user info
        AuditLogUtil.clear();
        return inventoryService.getInventoryByProductName(productName);
    }
    
    //GET inventory by productCategory
    @GetMapping("/productCategory/{productCategory}")
    public List<Inventory> getInventoryByProductCategory(@PathVariable ("productCategory") String productCategory) {
        logService.addWarnLog("InventoryController: Accssesing @GetMapping(productCategory) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getInventoryByProductCategory"); // Logs action with user info
        AuditLogUtil.clear();
        return inventoryService.getInventoryByProductCategory(productCategory);
    } 

    //Create an inventory item
    @PostMapping
    public Inventory createInventory(@RequestBody Inventory inventory) {
        logService.addWarnLog("InventoryController: Accssesing @PostMapping(createInventory) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: createInventory"); // Logs action with user info
        AuditLogUtil.clear();
        inventoryService.addNewInventory(inventory);
        return inventory;
    }


    //Update a inventory item
    @PutMapping(path = "{inventoryId}")
    public void updateInventory(
        @PathVariable ("inventoryId") Long inventoryId,
        @RequestParam(required = false) String productName,
        @RequestParam(required = false) int quantity,
        @RequestParam(required = false) BigDecimal price,
		@RequestParam(required = false) String productCategory,
		@RequestParam(required = false) String branch){
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: updateInventory"); // Logs action with user info
        AuditLogUtil.clear();
        inventoryService.updateInventory(inventoryId, productName, quantity, price, productCategory, branch);
        logService.addWarnLog("InventoryController: Accssesing @PutMapping(updateInventory) ");
        }


    //Delete inventory item by inventoryId
    @DeleteMapping(path = "{inventoryId}")
    public void deleteInventory(@PathVariable ("inventoryId") Long inventoryId){
        logService.addWarnLog("UserController: Accssesing @DeleteMapping(deleteUser) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: deleteInventory"); // Logs action with user info
        AuditLogUtil.clear();
        inventoryService.deleteInventory(inventoryId);

    }   

}
