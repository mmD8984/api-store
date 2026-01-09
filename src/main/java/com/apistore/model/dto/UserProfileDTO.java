package com.apistore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {

    private UUID id;
    private String email;
    private Boolean isEmailVerified;
    private String role;

    private Profile profile;

    @Data
    @Builder
    public static class Profile {
        private String fullName;
        private String phone;
        private String birthDate;
        private String avatarUrl;
    }
}
