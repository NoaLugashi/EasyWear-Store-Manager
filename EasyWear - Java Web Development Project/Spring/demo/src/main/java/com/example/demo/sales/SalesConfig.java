// package com.example.demo.sales;

// import java.math.BigDecimal;
// import java.util.List;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;


// @Configuration
// public class SalesConfig {


//     @Bean
//     public CommandLineRunner commandLineRunner(SalesRepository repository) {
//         return args -> {

//             // Define prices
//             BigDecimal price1 = new BigDecimal("50");
//             BigDecimal price2 = new BigDecimal("150");
//             BigDecimal price3 = new BigDecimal("250");

//             // Create transactions
//             Sales transaction1 = new Sales(
//                 "Moshe Cohen",
//                 "New",
//                 "Blue T-shirt",
//                 "Shirt",
//                 10,
//                 price1,
//                 "Tel-Aviv"
//             ); 

//             Sales transaction2 = new Sales(
//                 "Talya Levi",
//                 "VIP",
//                 "Black Pants",
//                 "Pants",
//                 1,
//                 price2,
//                 "Jerusalem"
//             ); 

//             Sales transaction3 = new Sales(
//                 "Ben Kaspi",
//                 "New",
//                 "Yellow Shoes",
//                 "Shoes",
//                 7,
//                 price3,
//                 "Haifa"
//             ); 

//             // Save transactions
//             repository.saveAll(List.of(transaction1, transaction2, transaction3));
//         };
//     }
// }
