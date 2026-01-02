package com.practice.project_1.service;// Utility/service class, or inside your UserService
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String extractMobileNumber(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject(); // mobile number stored as subject
        }
        throw new RuntimeException("Invalid or missing Authorization header");
    }
}
