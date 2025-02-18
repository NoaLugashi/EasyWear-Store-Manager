package com.example.demo.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    //equal to SQL QUERY: SELECT * FROM user WHERE username = ?
    //@Query("SELECT u FROM User u WHERE u.username = ?1") second option to do this
    Optional<User> findUserByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByBranch(String branch);
    
}
