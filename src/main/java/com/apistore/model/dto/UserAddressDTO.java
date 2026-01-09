package com.apistore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressDTO {

    private Integer id;
    private String recipientName;
    private String phone;
    private String addressLine;
    private String ward;
    private String district;
    private String province;
    private Boolean isDefault;
}
