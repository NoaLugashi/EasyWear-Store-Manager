// package com.example.demo.employee;

// import java.time.LocalDate;
// import java.time.Month;
// import java.util.List;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


// @Configuration
// public class EmployeeConfig {





//     @Bean
//     public CommandLineRunner commandLineRunner(EmployeeRepository employeeRepository) {
//         return args -> {
//             BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // Create BCryptPasswordEncoder instance


//             Long L1 = 123456L;
//             Long L2 = 654321L;
//             Long L3 = 312162142L;

//             Employee employee1 = new Employee(
//                 L1,
//                 "shlomos",
//                 "shlomo",
//                 "lala",
//                 encoder.encode("666666s"),
//                 "dfd@gmail.com",
//                 "Employee",
//                 "0545813509",
//                 "Tel-Aviv",
//                 LocalDate.of(2000, Month.AUGUST, 5),
//                 21
//             );

//             Employee employee2 = new Employee(
//                 L2,
//                 "michausr",
//                 "micha",
//                 "klum",
//                 encoder.encode("666666s"),
//                 "dsafds@gmail.com",
//                 "Employee",
//                 "0545813508",
//                 "Jerusalem",
//                 LocalDate.of(2000, Month.AUGUST, 5),
//                 21
//             );

//             Employee employee3 = new Employee(
//                 L3,
//                 "shakeds_nls",
//                 "shaked",
//                 "sabag",
//                 encoder.encode("61339125"),
//                 "sabag@gmail.com",
//                 "Employee",
//                 "0545813508",
//                 "Haifa",
//                 LocalDate.of(2000, Month.AUGUST, 5),
//                 21
//             );

//             // Save employees
//             employeeRepository.saveAll(List.of(employee1, employee2, employee3));
//         };
//     }
// }
