package com.example.demo.user;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.log.LogService;

import jakarta.transaction.Transactional;



@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final LogService logService;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    
    @Autowired
    public UserService(UserRepository userRepository, LogService logService) {
        this.userRepository = userRepository;
        this.logService = logService;
    }

    // GET method to fetch all users
    public List<User> getUser() {
        logService.addInfoLog("UserService: GET method to fetch all users start");
        return userRepository.findAll();
    }

    // POST method to add a new user
    public void addNewUser(User user) {
        logService.addInfoLog("UserService: POST method to add a new user start");
        Optional<User> userOptional = userRepository.findUserByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("Username is taken");
        }
        // Encode the password before saving
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        logService.addInfoLog("UserService: POST method to add a new user end");
    }

    // DELETE method to delete a user
    void deleteUser(Long userId) {
        logService.addInfoLog("UserService: DELETE method to delete a user start");
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("User with id " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
        logService.addInfoLog("UserService: DELETE method to delete a user ended with success");
    }

    // PUT method to update username, password, and role
    @Transactional
    public void updateUsername(Long userId, String username, String password, String role, String barnch) {
        logService.addInfoLog("UserService: PUT method to update user start");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));
        if (username != null && username.isEmpty() && !Objects.equals(user.getUsername(), username)) {
            user.setUsername(username);
        }
        if (password != null && password.isEmpty() && !Objects.equals(user.getPassword(), password)) {
            user.setPassword(encoder.encode(password)); // Encoding the updated password
        }
        if (role != null && role.isEmpty() && !Objects.equals(user.getRole(), role)) {
            user.setRole(role);
        }
        if (barnch != null && barnch.isEmpty() && !Objects.equals(user.getBranch(), barnch)) {
            user.setBranch(barnch);
        }
        logService.addInfoLog("UserService: PUT method to update user end");
    }

    // Fetch user by username for
    public User getUserByUsername(String username) {
        logService.addInfoLog("UserService: Fetch user by findByUsername start");
        return userRepository.findByUsername(username).orElse(null);
    }

    // Fetch user by userid for
    public User findUserById(Long userId) {
        logService.addInfoLog("UserService: Fetch user by findByUsername start");
        return userRepository.findById(userId).orElse(null);
    }

    // Fetch user by branch for
    public User getUserByBranch(String branch) {
        logService.addInfoLog("UserService: Fetch user by findByUsername start");
        return userRepository.findByBranch(branch).orElse(null);
    }


    
    // This method is part of the UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logService.addDebugLog("UserService: Attempting to load user by username: " + username);
        
        // Retrieve the user from the database
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {
            logService.addDebugLog("UserService: User not found: " + username);
            return new UsernameNotFoundException("UserService: User not found");
        });

        // Log the found user and their role
        logService.addDebugLog("UserService: User found: " + username + " with role: " + user.getRole());        

        // Create the authority object for the user's role
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
                
        // Return user details with roles and password
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // Password should already be encoded
                .authorities(authority) // Make sure roles are properly set
                .build();
    }	
	
}

