package com.example.demo.employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // This is a custom query method using Spring Data JPA's method name convention
    // This method finds an employee by employee ID (
    Optional<Employee> findByemployeenumber(Long employeenumber);

    // This is a method to find an employee by their username
    Optional<Employee> findByUsername(String username);

    // This is a method to find an employee by their branch
    Optional<Employee> findBybranch(String branch);

    // You can also use custom @Query annotations if necessary
    // Example of a custom query:
    //@Query("SELECT e FROM Employee e WHERE e.department = ?1")
}


