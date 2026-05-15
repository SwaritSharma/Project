package com.personal.project.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorLoginRequestDto {

    @NotBlank(message = "Vendor name cannot be blank")
    private String vendorName;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}