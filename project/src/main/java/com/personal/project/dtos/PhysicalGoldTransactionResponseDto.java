package com.personal.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhysicalGoldTransactionResponseDto {

    private Integer transactionId;

    private String vendorName;

    private String branchCity;

    private String branchState;

    private String quantity;

    private String deliveryStreet;

    private String deliveryCity;

    private String deliveryState;

    private String deliveryPostalCode;

    private String deliveryCountry;

    private String createdAt;
}