package com.example.demo.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.log.AuditLogUtil;
import com.example.demo.log.LogService;

@RestController
@RequestMapping(path = "api/v1/User")
public class UserController {

    private final UserService userService;
    private final LogService logService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @GetMapping
	public List <User> getUser() {
        logService.addWarnLog("UserController: Accssesing @GetMapping(getUser) ");
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: GetUserAdmin"); // Logs action with user info
        AuditLogUtil.clear();
		return userService.getUser();
	}

    @PostMapping
    public void registerNewUser (@RequestBody User user) {
        logService.addWarnLog("UserController: Accssesing @PostMapping(addNewUser) ");
        AuditLogUtil.setCurrentUser();
        userService.addNewUser(user);
        logger.info("Action performed: addNewUser"); // Logs action with user info
        AuditLogUtil.clear();
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable ("userId") Long userId){
        logService.addWarnLog("UserController: Accssesing @DeleteMapping(deleteUser) ");
        AuditLogUtil.setCurrentUser();
        userService.deleteUser(userId);
        logger.info("Action performed: deleteUser"); // Logs action with user info
        AuditLogUtil.clear();
    }


    @PutMapping(path = "{userId}")
    public void updateUser(
        @PathVariable("userId") Long userId,
        @RequestBody User user) {
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: updateUser"); // Logs action with user info
    
        // Call the updateUsername function in the UserService
        userService.updateUsername(
            userId, 
            user.getUsername(), 
            user.getPassword(), 
            user.getRole(), 
            user.getBranch()
        );
    
        // Log the action after updating
        logService.addWarnLog("UserController: Accessing @PutMapping(updateUsername)");
    
        AuditLogUtil.clear();
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String username, @RequestParam String password) {
        AuditLogUtil.setCurrentUser();
        logger.info("Action performed: authenticateUser login"); // Logs action with user info
        AuditLogUtil.clear();        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        if (isAuthenticated) {
            return "Authentication successful!";
        } else {
            return "Authentication failed!";
        }
    }

}
