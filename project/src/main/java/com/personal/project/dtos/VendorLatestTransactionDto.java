package com.personal.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorLatestTransactionDto {

    private Integer transactionId;

    private String transactionType;

    private String transactionStatus;

    private String branchCity;

    private String branchState;

    private String quantity;

    private String amount;

    private String createdAt;
}