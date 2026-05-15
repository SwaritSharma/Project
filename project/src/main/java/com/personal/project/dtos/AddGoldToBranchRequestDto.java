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
public class AddGoldToBranchRequestDto {

    @NotNull(message = "Gold quantity cannot be null")
    @DecimalMin(
            value = "0.01",
            message = "Gold quantity must be greater than 0"
    )
    private BigDecimal goldQuantity;
}