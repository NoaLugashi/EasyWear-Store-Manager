package com.example.demo.inventory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByInventoryId(Long inventoryId);  // Fetch inventory by id

    Optional<Inventory> findByProductName(String productName); // Fetch inventory by name

    List<Inventory> findByproductCategory(String productCategory); // Fetch inventory by category

    List<Inventory> findBybranch(String branch); // Fetch inventory by branch

}

