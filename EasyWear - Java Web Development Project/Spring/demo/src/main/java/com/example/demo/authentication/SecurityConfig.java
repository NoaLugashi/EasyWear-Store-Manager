package com.example.demo.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.log.LogService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LogService logService;

    private final CombinedUserDetailsService combinedUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(CombinedUserDetailsService combinedUserDetailsService, 
                          PasswordEncoder passwordEncoder,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                          LogService logService) {
        this.combinedUserDetailsService = combinedUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.logService = logService;
    }

    // Global security filter chain to configure HTTP security rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            // Allow access to login page, failure page, logout, and static resources
            .requestMatchers("/login.html", "/login2.html", "/login-failure.html", "/logout.html", "/css/**", "/js/**", "/image/**").permitAll()
            // .requestMatchers("/api/v1/User").permitAll()
            .requestMatchers("/api/v1/User/**").authenticated()
            // .requestMatchers("/api/v1/employees").permitAll()
            .requestMatchers("/api/v1/employees/**").authenticated()
            .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login.html").permitAll() // Define the custom login page
                .successHandler(customAuthenticationSuccessHandler) // Custom success handler
                .failureUrl("/login-failure.html")  // Redirect to failure page
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");  // Redirect after logout
        return http.build();
    }

    // Bean to create the AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        logService.addDebugLog("SecurityConfig: Building an AuthenticationManager... ");
        AuthenticationManagerBuilder authenticationManagerBuilder = 
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // Use CombinedUserDetailsService for both User and Employee authentication
        authenticationManagerBuilder
            .userDetailsService(combinedUserDetailsService)
            .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }


}
