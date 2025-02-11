// package com.example.demo.inventory;

// import java.math.BigDecimal;
// import java.util.List;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;


// @Configuration
// public class InventoryConfig {


//     @Bean
//     public CommandLineRunner commandLineRunner(InventoryRepository repository) {
//         return args -> {

//             // Define prices
//             BigDecimal price1 = new BigDecimal("50");
//             BigDecimal price2 = new BigDecimal("150");

//             // Create inventories
//             Inventory inventory1 = new Inventory(
//                 "Blue T-shirt",
//                 100,
//                 price1,
//                 "Shirts",
//                 "Tel-Aviv"
//             ); 

//             Inventory inventory2 = new Inventory(
//                 "Black Pants",
//                 200,
//                 price2,
//                 "Pants",
//                 "Jerusalem"
//             ); 

//             Inventory inventory3 = new Inventory(
//                 "Winter Coat Black",
//                 75,
//                 price2,
//                 "Coats",
//                 "Haifa"
//             ); 

//             // Save inventories
//             repository.saveAll(List.of(inventory1, inventory2, inventory3));
//         };
//     }
// }
