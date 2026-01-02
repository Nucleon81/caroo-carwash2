package com.practice.project_1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // Uses the separate CorsConfig bean
                .authorizeHttpRequests(auth -> auth
                        // 1) Allow static resources (admin panel + other static)
                        .requestMatchers(
                                "/admin/**",        // login.html, index.html, style.css, script.js under /static/admin
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()

                        // 2) Public API endpoints (no auth required)
                        .requestMatchers(HttpMethod.GET, "/api/services").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/bookings/slots").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/bookings/addons").permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/auth/verify-otp"
                        ).permitAll()

                        // 3) Authenticated endpoints (JWT required)
                        .requestMatchers("/api/auth/update-credentials").authenticated()
                        .requestMatchers("/api/user/**").authenticated()
                        .requestMatchers("/api/bookings/**").authenticated()

                        // 4) Admin-only API endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtSecret), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
