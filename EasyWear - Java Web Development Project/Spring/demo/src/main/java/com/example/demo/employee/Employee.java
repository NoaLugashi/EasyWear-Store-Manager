package com.example.demo.employee;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {

    // employeeId is unique, but not the primary key
    @Column(name = "employeeId", unique = true, nullable = false)
    private Long employeeId;

    // employeenumber is the auto-incremented primary key
    // Sequence for generating employee number
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "employee_number_seq")
    @SequenceGenerator(
        name = "employee_number_seq",      // Name of the sequence
        sequenceName = "employee_number_sequence", // Sequence name in the DB
        allocationSize = 1)                  // Increment step 
    @Column(name = "employeenumber", unique = true, nullable = false)
    private Long employeenumber;
    
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String role;
    private String phone;
    private LocalDate dob;
    private Integer age;
    private String branch;


    // Empty constructor
    public Employee() {
    }

    // Full Constructor 
    public Employee(Long employeeId, Long employeenumber, String username, String firstName, String lastName, String password, String email, String role, String phone, String branch,
    LocalDate dob, Integer age) {
        this.employeeId = employeeId;
        this.employeenumber = employeenumber;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.branch = branch;
        this.dob = dob;
        this.age = age;
    }

    // Constructor without employeenumber (it will be manually assigned by the DB)
    public Employee(Long employeeId, String username, String firstName, String lastName, String password, String email, String role, String phone, String branch, LocalDate dob, Integer age) {
        this.employeeId = employeeId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.branch = branch;
        this.dob = dob;
        this.age = age;
    }


    // Getters
    public Long getEmployeeId() {
        return this.employeeId;
    }

    public Long getEmployeeNumber() {
        return this.employeenumber;
    }
    
    public String getusername() {
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getRole() {
        return this.role;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getBranch() {
        return this.branch;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public Integer getAge() {
        return this.age;
    }


    // Setters
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeNumber(Long employeenumber) {
        this.employeenumber = employeenumber;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "{" +
            " Employee id='" + getEmployeeId() + "'" +
            " Employee Number='" + getEmployeeNumber() + "'" +
            " Employee username='" + getusername() + "'" +
            ", First Name='" + getFirstName() + "'" +
            ", Last Name='" + getLastName() + "'" +
            ", Password ='" + getPassword() + "'" +
            ", Email='" + getEmail() + "'" +
            ", Role='" + getRole() + "'" +
            ", Phone='" + getPhone() + "'" +
            ", Branch='" + getBranch() + "'" +
            ", Dob='" + getDob() + "'" +
            ", Age='" + getAge() + "'" +
            "}";
    }
    
}