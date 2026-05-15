package com.personal.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionHistoryResponseDto {

    private Integer transactionId;

    private String transactionType;

    private String transactionStatus;

    private String vendorName;

    private String branchCity;

    private String branchState;

    private String quantity;

    private String amount;

    private String createdAt;
}