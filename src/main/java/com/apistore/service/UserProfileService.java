package com.apistore.service;

import com.apistore.model.dto.UserProfileDTO;
import com.apistore.model.dto.UpdateProfileRequest;
import com.apistore.model.entity.User;
import com.apistore.model.entity.UserProfile;
import com.apistore.repository.UserProfileRepository;
import com.apistore.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;

    public UserProfileService(UserRepository userRepository,
                              UserProfileRepository profileRepository,
                              CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    /* ================= GET PROFILE ================= */
    public UserProfileDTO getProfile(String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = profileRepository.findByUserId(user.getId())
                .orElseGet(() -> createEmptyProfile(user.getId()));

        return mapToResponse(user, profile);
    }

    /* ================= UPDATE PROFILE ================= */
    @Transactional
    public UserProfileDTO updateProfile(String firebaseUid, UpdateProfileRequest request) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = profileRepository.findByUserId(user.getId())
                .orElseGet(() -> createEmptyProfile(user.getId()));

        if (request.getProfile() != null) {
            profile.setFullName(request.getProfile().getFullName());
            profile.setPhone(request.getProfile().getPhone());

            if (request.getProfile().getBirthDate() != null) {
                profile.setBirthDate(request.getProfile().getBirthDate());
            }
        }

        profileRepository.save(profile);
        return mapToResponse(user, profile);
    }

    /* ================= UPDATE AVATAR ================= */
    @Transactional
    public UserProfileDTO updateAvatar(String firebaseUid, String avatarUrl) {
        try {
            User user = userRepository.findByFirebaseUid(firebaseUid)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserProfile profile = profileRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            profile.setAvatarUrl(avatarUrl);
            profileRepository.save(profile);

            return mapToResponse(user, profile);
        } catch (Exception e) {
            throw new RuntimeException("Upload avatar failed", e);
        }
    }

    /* ================= HELPERS ================= */

    private UserProfile createEmptyProfile(UUID userId) {
        UserProfile profile = UserProfile.builder()
                .userId(userId)
                .build();
        return profileRepository.save(profile);
    }

    private UserProfileDTO mapToResponse(User user, UserProfile profile) {
        return UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .isEmailVerified(user.getIsEmailVerified())
                .role(user.getRole())
                .profile(
                        UserProfileDTO.Profile.builder()
                                .fullName(profile.getFullName())
                                .phone(profile.getPhone())
                                .birthDate(
                                        profile.getBirthDate() != null
                                                ? profile.getBirthDate().toString()
                                                : null
                                )
                                .avatarUrl(profile.getAvatarUrl())
                                .build()
                )
                .build();
    }
}
