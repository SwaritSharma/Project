package com.personal.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private Integer paymentId;

    private String paymentMethod;

    private String transactionType;

    private String paymentStatus;

    private String amount;

    private String createdAt;
}