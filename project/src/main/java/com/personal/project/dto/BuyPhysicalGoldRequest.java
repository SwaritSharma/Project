package com.personal.project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyPhysicalGoldRequest {

    @NotNull(
            message = "User id is required"
    )
    private Integer userId;

    @NotNull(
            message = "Vendor id is required"
    )
    private Integer vendorId;

    @NotNull(
            message = "Quantity is required"
    )
    @DecimalMin(
            value = "0.1",
            message = "Quantity must be greater than 0"
    )
    private BigDecimal quantity;

    @NotNull(
            message = "Delivery address id is required"
    )
    private Integer deliveryAddressId;
}