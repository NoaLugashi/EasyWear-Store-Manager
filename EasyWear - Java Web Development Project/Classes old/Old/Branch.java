// package com.example.demo.branch;


// import java.util.List;

// import com.example.demo.employee.Employee;
// import com.fasterxml.jackson.annotation.JsonBackReference;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.SequenceGenerator;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "branches")
// public class Branch {

//     @Id
//     @SequenceGenerator(
//         name = "branch_sequence",
//         sequenceName = "branch_sequence",
//         allocationSize = 1,
//         initialValue = 100 // Start at 100 to ensure 3-digit IDs
//     )
//     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_sequence")
//     @Column(name = "branch_id", unique = true, nullable = false)
//     private Integer branchId;

//     @Column(name = "branchName", unique = true, nullable = false)
//     private String branchName;

//     private String branchLocation;

//     // @JsonIgnoreProperties({"employees"}) // Ignore the employees field during serialization
//     @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
//     @JsonBackReference
//     private List<Employee> employees;
    
//     // Empty constructor
//     public Branch() {
//     }

//     // Constructor with all fields
//     public Branch(Integer branchId, String branchName) {
//         this.branchId = branchId;
//         this.branchName = branchName;
//     }

//     // Constructor without ID
//     public Branch(String branchName, String branchLocation) {
//         this.branchName = branchName;
//         this.branchLocation = branchLocation;
//     }

//     // Getters
//     public Integer getBranchId() {
//         return branchId;
//     }

//     public String getBranchName() {
//         return branchName;
//     }

//     public String getBranchLocation() {
//         return branchLocation;
//     }

//     public List<Employee> getEmployees() {
//         return employees;
//     }

//     // Setters
//     public void setBranchId(Integer branchId) {
//         this.branchId = branchId;
//     }

//     public void setBranchName(String branchName) {
//         this.branchName = branchName;
//     }

//     public void setBranchLocation(String branchLocation) {
//         this.branchLocation = branchLocation;
//     }

//     public void setEmployees(List<Employee> employees) {
//         this.employees = employees;
//     }


//     @Override
//     public String toString() {
//         return "{" +
//             " Branch id='" + getBranchId() + "'" +
//             ", Branch Name='" + getBranchName() + "'" +
//             ", Branch Location='" + getBranchLocation() + "'" +
//             "}";
//     }
// }

