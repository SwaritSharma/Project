package com.personal.project.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellVirtualGoldRequestDto {

    @NotNull(message = "Vendor ID cannot be null")
    private Integer vendorId;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(
            value = "0.01",
            message = "Quantity must be greater than 0"
    )
    private BigDecimal quantity;
}