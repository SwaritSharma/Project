package com.personal.project.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDashboardResponseDto {

    private Integer userId;

    private String name;

    private String email;

    private BigDecimal balance;

    private String street;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private List<LatestTransactionDto> latestTransactions;
}