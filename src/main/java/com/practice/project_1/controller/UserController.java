package com.practice.project_1.controller;

import com.practice.project_1.model.Users;
import com.practice.project_1.repository.UserRepository;
import com.practice.project_1.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String mobileNumber = jwtService.extractMobileNumber(authHeader);
            Users user = userRepository.findById(mobileNumber)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            // You can return only the address if you want:
            // return ResponseEntity.ok(Map.of("userLocation", user.getUserLocation()));
            // But usually profile is needed:
            return ResponseEntity.ok(Map.of("userLocation",user.getUserLocation()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }
    }
    @PostMapping("/api/user/register-fcm-token")
    public ResponseEntity<Void> registerFcmToken(
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        String token = body.get("fcmToken");
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String mobile = authentication.getName(); // or userId from JWT
        Users user = userRepository.findByMobileNumber(mobile)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFcmToken(token);
        userRepository.save(user);

        return ResponseEntity.ok().build();

}


}