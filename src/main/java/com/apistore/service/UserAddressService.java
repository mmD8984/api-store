package com.apistore.service;

import com.apistore.model.dto.UserAddressDTO;
import com.apistore.model.entity.User;
import com.apistore.model.entity.UserAddress;
import com.apistore.repository.UserAddressRepository;
import com.apistore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserAddressService {

    private final UserRepository userRepository;
    private final UserAddressRepository addressRepository;

    public UserAddressService(UserRepository userRepository,
                              UserAddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    /* ========== GET ADDRESSES ========== */
    public List<UserAddressDTO> getAddresses(String firebaseUid) {
        User user = getUser(firebaseUid);

        return addressRepository.findByUserIdOrderByIsDefaultDescCreatedAtDesc(user.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /* ========== ADD ADDRESS ========== */
    @Transactional
    public UserAddressDTO addAddress(String firebaseUid, UserAddressDTO req) {
        User user = getUser(firebaseUid);

        if (Boolean.TRUE.equals(req.getIsDefault())) {
            addressRepository.clearDefault(user.getId());
        }

        UserAddress address = UserAddress.builder()
                .userId(user.getId())
                .recipientName(req.getRecipientName())
                .phone(req.getPhone())
                .addressLine(req.getAddressLine())
                .ward(req.getWard())
                .district(req.getDistrict())
                .province(req.getProvince())
                .isDefault(req.getIsDefault())
                .build();

        return toDTO(addressRepository.save(address));
    }

    /* ========== UPDATE ADDRESS ========== */
    @Transactional
    public UserAddressDTO updateAddress(String firebaseUid, Integer id, UserAddressDTO req) {
        User user = getUser(firebaseUid);

        UserAddress address = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (Boolean.TRUE.equals(req.getIsDefault())) {
            addressRepository.clearDefault(user.getId());
        }

        address.setRecipientName(req.getRecipientName());
        address.setPhone(req.getPhone());
        address.setAddressLine(req.getAddressLine());
        address.setWard(req.getWard());
        address.setDistrict(req.getDistrict());
        address.setProvince(req.getProvince());
        address.setIsDefault(req.getIsDefault());

        return toDTO(addressRepository.save(address));
    }

    /* ========== SET DEFAULT ========== */
    @Transactional
    public UserAddressDTO setDefault(String firebaseUid, Integer id) {
        User user = getUser(firebaseUid);

        addressRepository.clearDefault(user.getId());

        UserAddress address = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setIsDefault(true);
        return toDTO(addressRepository.save(address));
    }

    /* ========== DELETE ========== */
    @Transactional
    public void delete(String firebaseUid, Integer id) {
        User user = getUser(firebaseUid);

        UserAddress address = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            throw new RuntimeException("Cannot delete default address");
        }

        addressRepository.delete(address);
    }

    /* ========== HELPERS ========== */
    private User getUser(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserAddressDTO toDTO(UserAddress a) {
        return UserAddressDTO.builder()
                .id(a.getId())
                .recipientName(a.getRecipientName())
                .phone(a.getPhone())
                .addressLine(a.getAddressLine())
                .ward(a.getWard())
                .district(a.getDistrict())
                .province(a.getProvince())
                .isDefault(a.getIsDefault())
                .build();
    }
}
