package com.personal.project.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorRegistrationRequestDto {

    @NotBlank(message = "Vendor name cannot be blank")
    private String vendorName;

    private String description;

    @NotBlank(message = "Contact person name cannot be blank")
    private String contactPersonName;

    @Email(message = "Invalid email format")
    private String contactEmail;

    private String contactPhone;

    private String websiteUrl;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}