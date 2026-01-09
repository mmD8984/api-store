package com.apistore.controller;

import com.apistore.model.dto.UpdateProfileRequest;
import com.apistore.model.dto.UserProfileDTO;
import com.apistore.service.UserProfileService;
import com.apistore.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/user/profile")
public class UserProfileController {

    private final UserProfileService profileService;
    private final CloudinaryService cloudinaryService;

    public UserProfileController(UserProfileService profileService,
                                 CloudinaryService cloudinaryService) {
        this.profileService = profileService;
        this.cloudinaryService = cloudinaryService;
    }

    /* ========== GET PROFILE ========== */
    @GetMapping
    public ResponseEntity<?> getProfile(@RequestAttribute("firebaseUid") String firebaseUid) {
        return ResponseEntity.ok(profileService.getProfile(firebaseUid));
    }

    /* ========== UPDATE PROFILE ========== */
    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestAttribute("firebaseUid") String firebaseUid,
            @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(profileService.updateProfile(firebaseUid, request));
    }

    /* ========== UPLOAD AVATAR ========== */
    @PutMapping("/avatar")
    public ResponseEntity<UserProfileDTO > uploadAvatar(
            @RequestAttribute("firebaseUid") String firebaseUid,
            @RequestParam("file") MultipartFile file
    ) {

        try {
            // Upload ảnh lên Cloudinary
            Map uploadResult = cloudinaryService.upload(file);

            String avatarUrl = (String) uploadResult.get("secure_url");

            // Cập nhật avatar URL vào DB
            return ResponseEntity.ok(
                    profileService.updateAvatar(firebaseUid, avatarUrl)
            );

        } catch (Exception e) {
            throw new RuntimeException("Upload avatar failed", e);
        }
    }

}
