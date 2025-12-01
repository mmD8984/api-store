package com.apistore.service;

import com.apistore.model.dto.UserDTO;
import com.apistore.model.entity.User;
import com.apistore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Tạo user mới nếu chưa có hoặc lấy user hiện tại
    public UserDTO createOrGetUser(String firebaseUid, String email, Boolean isEmailVerified) {
        Optional<User> optionalUser = userRepository.findByFirebaseUid(firebaseUid);

        User user = optionalUser.orElseGet(() -> {
            User newUser = User.builder()
                    .firebaseUid(firebaseUid)
                    .email(email)
                    .isEmailVerified(isEmailVerified)
                    .role("user")
                    .build();
            return userRepository.save(newUser);
        });

        return UserDTO.builder()
                .id(user.getId())
                .firebaseUid(user.getFirebaseUid())
                .email(user.getEmail())
                .isEmailVerified(user.getIsEmailVerified())
                .role(user.getRole())
                .build();
    }

    // --- Cập nhật trạng thái email đã xác thực ---
    @Transactional
    public void updateEmailVerified(String uid, boolean isVerified) {
        userRepository.updateEmailVerified(uid, isVerified);
    }
}
