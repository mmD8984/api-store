package com.apistore.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {

    private Profile profile;

    @Data
    public static class Profile {
        private String fullName;
        private String phone;
        private LocalDate birthDate;
    }
}