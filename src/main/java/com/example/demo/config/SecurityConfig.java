package com.example.demo.config;

import com.example.demo.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        // --- ENDPOINTS PUBLIC---
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/hello/public").permitAll()
                        .requestMatchers("/jobs").permitAll()   // liste des offres accessible à tous
                        // --- ENDPOINTS ROLE ---
                        .requestMatchers("/hello/private-admin").hasRole("ADMIN")
                        .requestMatchers("/jobs/add").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/jobs/delete/**").authenticated()
                        // --- AUTRES ---
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtService, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
