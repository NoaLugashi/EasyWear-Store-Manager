// package com.example.demo.user;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpMethod;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// public class UserSecurityConfig {

//     private final UserService userService;
//     private final PasswordEncoder passwordEncoder; // This will be injected by Spring

//     @Autowired
//     public UserSecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
//         this.userService = userService;
//         this.passwordEncoder = passwordEncoder;
//     }

//     // Security filter chain to configure HTTP security rules
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.csrf().disable()
//             .authorizeHttpRequests()

//             // Allow access to login page, success page, and static resources
//             .requestMatchers("/login.html", "/login-failure.html", 
//             "/logout.html", "/css/**", "/js/**", "/images/**").permitAll()
            
//             // Allow API endpoints like GET, POST, PUT, DELETE for public access or authenticated users
//             .requestMatchers("/api/v1/User").permitAll()
//             .requestMatchers("/api/v1/User/**").authenticated()
//             .requestMatchers(HttpMethod.DELETE, "/api/v1/User/**").permitAll()  // Allow DELETE without authentication
//             .requestMatchers(HttpMethod.PUT, "/api/v1/User/**").permitAll()  // Allow PUT without authentication
//             .requestMatchers(HttpMethod.POST, "/api/v1/User/**").permitAll()  // Allow POST without authentication
//             .requestMatchers(HttpMethod.GET, "/api/v1/User/**").permitAll()  // Allow GET without authentication
//             .requestMatchers("/api/v1/employees").permitAll()
//             .requestMatchers("/api/v1/employees/**").authenticated()
//             .requestMatchers(HttpMethod.DELETE, "/api/v1/employees/**").permitAll()  // Allow DELETE without authentication
//             .requestMatchers(HttpMethod.PUT, "/api/v1/employees/**").permitAll()  // Allow PUT without authentication
//             .requestMatchers(HttpMethod.POST, "/api/v1/employees/**").permitAll()  // Allow POST without authentication
//             .requestMatchers(HttpMethod.GET, "/api/v1/employees/**").permitAll()  // Allow GET without authentication
//             .anyRequest().authenticated()
//             .and()
//             .formLogin()
//                 .loginPage("/login.html").permitAll()// Define the custom login page
//                 .defaultSuccessUrl("/AdminPanel.html", true)  // Redirect after successful login
//                 .failureUrl("/login-failure.html")  // Redirect to login page on failure
//             .and()
//             .logout()
//                 .logoutUrl("/logout")
//                 .logoutSuccessUrl("/login")  // Redirect after logout
//             .and()
//             .sessionManagement()
//                 .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);  // Optional: Set to IF_REQUIRED if you are managing sessions
//         return http.build();
//     }

//     // AuthenticationManager bean for authentication setup
//     @Bean
//     public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//         AuthenticationManagerBuilder authenticationManagerBuilder = 
//                 http.getSharedObject(AuthenticationManagerBuilder.class);
//         authenticationManagerBuilder
//             .userDetailsService(userService)
//             .passwordEncoder(passwordEncoder);  // Use the password encoder from the bean   
//         return authenticationManagerBuilder.build();
//     }
// }
