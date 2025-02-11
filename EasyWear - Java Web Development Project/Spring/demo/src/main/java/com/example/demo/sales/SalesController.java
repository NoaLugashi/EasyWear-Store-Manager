package com.example.demo.sales;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.log.AuditLogUtil;
import com.example.demo.log.LogService;

@RestController
@RequestMapping(path = "api/v1/sales")
public class SalesController {

    private final SalesService salesService;
    private final LogService logService; 
    private static final Logger logger = LoggerFactory.getLogger(SalesController.class);

    public SalesController(SalesService salesService, LogService logService) {
        this.salesService = salesService;
        this.logService = logService;
    }

    //GET all sales
    @GetMapping
    public List <Sales> getSales() {
        logService.addWarnLog("SalesController: Accssesing @GetMapping(getSales) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getSales"); // Logs action with user info
        AuditLogUtil.clear();
        return salesService.getSales();
    }

    //GET sales by branch
    @GetMapping("/branch/{branch}")
    public List<Sales> getSalesByBranch(@PathVariable ("branch") String branch) {
        logService.addWarnLog("SalesController: Accssesing @GetMapping(branch) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getSalesByBranch"); // Logs action with user info
        AuditLogUtil.clear();
        return salesService.getSalesBybranch(branch);
    }
    
    //GET sales by productName
    @GetMapping("/productName/{productName}")
    public List<Sales> getsalesByProductName(@PathVariable ("productName") String productName) {
        logService.addWarnLog("SalesController: Accssesing @GetMapping(productName) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getsalesByProductName"); // Logs action with user info
        AuditLogUtil.clear();
        return salesService.getSalesByProductName(productName);
    }
    
    //GET sales by productCategory
    @GetMapping("/productCategory/{productCategory}")
    public List<Sales> getSalesByProductCategory(@PathVariable ("productCategory") String productCategory) {
        logService.addWarnLog("SalesController: Accssesing @GetMapping(productCategory) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: getSalesByProductCategory"); // Logs action with user info
        AuditLogUtil.clear();
        return salesService.getSalesByProductCategory(productCategory);
    } 

    //Create a sale transaction item
    @PostMapping
    public Sales createSales(@RequestBody Sales Sale) {
        logService.addWarnLog("SalesController: Accssesing @PostMapping(createSales) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: createSales"); // Logs action with user info
        AuditLogUtil.clear();
        salesService.addNewSales(Sale);
        return Sale;
    }

    //Delete sale transaction by id
    @DeleteMapping(path = "{id}")
    public void deleteSales(@PathVariable ("id") Long id){
        logService.addWarnLog("SalesController: Accssesing @DeleteMapping(deleteSales) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: deleteSales"); // Logs action with user info
        AuditLogUtil.clear();
        salesService.deleteSales(id);

    }

    // Create a sale transaction (buying a product)
    @PostMapping("/buy")
    public ResponseEntity<String> buyProduct(@RequestParam Long customerId, 
                                            @RequestParam String productName, 
                                            @RequestParam int quantity) {
        logService.addWarnLog("SalesController: Accessing @PostMapping(buyProduct) ");
        logService.addInfoLog("SalesController: Received product name: " + productName);
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: buyProduct"); // Logs action with user info
        AuditLogUtil.clear();

        try {
            // Call the buy method from SalesService
            salesService.buy(customerId, productName, quantity);

            // If successful, return a success message
            return ResponseEntity.ok("Purchase successful!");
        } catch (Exception e) {
            // If an error occurs, return an error message
            logService.addWarnLog("Error during purchase: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the purchase: " + e.getMessage());
        }
    }


}
