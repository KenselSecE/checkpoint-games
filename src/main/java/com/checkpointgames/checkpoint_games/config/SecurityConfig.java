package com.checkpointgames.checkpoint_games.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Admin area: ADMIN only
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Create/edit products: STAFF or ADMIN (must come BEFORE "/products/*")
                        .requestMatchers("/products/new", "/products/*/edit").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products", "/products/*/edit").hasAnyRole("STAFF", "ADMIN")
                        // Public pages
                        .requestMatchers("/", "/about", "/info", "/register", "/login", "/access-denied", "/error").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products", "/products/*").permitAll()
                        // Everything else requires login
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());   // default login page for now
        return http.build();
    }
}