// package com.example.demo.branch;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class BranchConfig {

//     private final BranchService branchService;

//     public BranchConfig(BranchService branchService) {
//         this.branchService = branchService;
//     }

//     @Bean(name = "commandLineRunnerBranch")
//     public CommandLineRunner commandLineRunner() {
//         return args -> {
//             // Create branches by calling the service
//             branchService.createBranches();
//         };
//     }
// }
