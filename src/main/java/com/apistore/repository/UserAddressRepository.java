package com.apistore.repository;

import com.apistore.model.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {

    List<UserAddress> findByUserIdOrderByIsDefaultDescCreatedAtDesc(UUID userId);

    Optional<UserAddress> findByIdAndUserId(Integer id, UUID userId);

    @Modifying
    @Query("""
        UPDATE UserAddress a
        SET a.isDefault = false
        WHERE a.userId = :userId AND a.isDefault = true
    """)
    void clearDefault(UUID userId);
}