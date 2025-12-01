package com.apistore.repository;

import com.apistore.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Tìm user bằng firebase UID
    Optional<User> findByFirebaseUid(String firebaseUid);

    // Tìm user bằng email
    Optional<User> findByEmail(String email);

    // Kiểm tra email đã tồn tại hay chưa
    boolean existsByEmail(String email);

    // Kiểm tra firebase uid có tồn tại không
    boolean existsByFirebaseUid(String firebaseUid);

    // --- Thêm method cập nhật isEmailVerified ---
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isEmailVerified = :isVerified WHERE u.firebaseUid = :uid")
    void updateEmailVerified(@Param("uid") String uid, @Param("isVerified") boolean isVerified);
}
