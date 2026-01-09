package com.apistore.controller;

import com.apistore.model.dto.UserAddressDTO;
import com.apistore.service.UserAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/addresses")
public class UserAddressController {

    private final UserAddressService addressService;

    public UserAddressController(UserAddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<UserAddressDTO> getAddresses(
            @RequestAttribute("firebaseUid") String firebaseUid
    ) {
        return addressService.getAddresses(firebaseUid);
    }

    @PostMapping
    public UserAddressDTO addAddress(
            @RequestAttribute("firebaseUid") String firebaseUid,
            @RequestBody UserAddressDTO request
    ) {
        return addressService.addAddress(firebaseUid, request);
    }

    @PutMapping("/{id}")
    public UserAddressDTO updateAddress(
            @RequestAttribute("firebaseUid") String firebaseUid,
            @PathVariable Integer id,
            @RequestBody UserAddressDTO request
    ) {
        return addressService.updateAddress(firebaseUid, id, request);
    }

    @PutMapping("/{id}/default")
    public UserAddressDTO setDefault(
            @RequestAttribute("firebaseUid") String firebaseUid,
            @PathVariable Integer id
    ) {
        return addressService.setDefault(firebaseUid, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @RequestAttribute("firebaseUid") String firebaseUid,
            @PathVariable Integer id
    ) {
        addressService.delete(firebaseUid, id);
        return ResponseEntity.noContent().build();
    }
}
