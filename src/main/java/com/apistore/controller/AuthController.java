package com.apistore.controller;

import com.apistore.model.dto.UserDTO;
import com.apistore.service.FirebaseAuthService;
import com.apistore.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    @Autowired
    private UserService userService;

    /**
     * Endpoint login/register bằng Firebase Auth
     * Frontend gửi ID Token Firebase trong header Authorization: Bearer <token>
     */
    @PostMapping("/firebase-login")
    public ResponseEntity<?> firebaseLogin(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
            }

            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = firebaseAuthService.verifyIdToken(token);

            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            Boolean emailVerified = decodedToken.isEmailVerified();

            // Tạo hoặc lấy user từ DB
            UserDTO userDTO = userService.createOrGetUser(uid, email, emailVerified);

            return ResponseEntity.ok(userDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Invalid Firebase Token");
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> body) {
        try {
            String uid = body.get("uid"); // UID Firebase user

            // Lấy user từ Firebase
            UserRecord user = FirebaseAuth.getInstance().getUser(uid);
            boolean isVerified = user.isEmailVerified();

            // Cập nhật cột is_email_verified trong DB
            userService.updateEmailVerified(uid, isVerified);

            return ResponseEntity.ok(Map.of("isEmailVerified", isVerified));
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

}
