package com.personal.project.dtos;

import com.personal.project.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMoneyToWalletRequestDto {

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(
            value = "0.01",
            message = "Amount must be greater than 0"
    )
    private BigDecimal amount;

    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;
}