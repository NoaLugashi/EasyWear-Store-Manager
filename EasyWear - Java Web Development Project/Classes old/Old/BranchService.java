// package com.example.demo.branch;

// import java.util.List;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.example.demo.log.LogService;

// @Service
// public class BranchService {

//     private final BranchRepository branchRepository;

//     private final LogService logService;

//     public BranchService(BranchRepository branchRepository, LogService logService) {
//         this.branchRepository = branchRepository;
//         this.logService = logService;
//     }

//     @Transactional
//     public List<Branch> createBranches() {
//         logService.addInfoLog("BranchService: CreateBranches start");
//         // Create and save branches to the repository if not already saved
//         Branch branchA = new Branch("Branch A", "Location A");
//         Branch branchB = new Branch("Branch B", "Location B");
//         Branch branchC = new Branch("Branch C", "Location C");

//         // Save branches to the repository
//         branchRepository.saveAll(List.of(branchA, branchB, branchC));
        
//         // Return the created branches
//         return List.of(branchA, branchB, branchC);
//     }
// }
