package com.personal.project.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterVendorBranchRequestDto {

    @NotBlank(message = "Street cannot be blank")
    private String street;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "State cannot be blank")
    private String state;

    private String postalCode;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(
            value = "0.0",
            message = "Quantity cannot be negative"
    )
    private BigDecimal quantity;
}