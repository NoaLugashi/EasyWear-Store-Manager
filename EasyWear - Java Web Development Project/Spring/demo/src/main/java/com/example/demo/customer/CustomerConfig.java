// package com.example.demo.customer;

// import java.util.List;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class CustomerConfig {


//     @Bean
//     public CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
//         return args -> {


//             // Create inventories
//             Customer customer1 = new Customer(
//                 "Moshe Cohen",
//                 "111222333",
//                 "054-11122233",
//                 "New"
//             ); 

//             Customer customer2 = new Customer(
//                 "Talya Levi",
//                 "222333444",
//                 "054-22233344",
//                 "VIP"
//             ); 

//             Customer customer3 = new Customer(
//                 "Ben Kaspi",
//                 "333444555",
//                 "052-33344455",
//                 "Returning"
//             ); 

//             // Save customers
//             customerRepository.saveAll(List.of(customer1, customer2, customer3));
//         };
//     }

// }
