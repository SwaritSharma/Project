package com.personal.project.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorBranchResponseDto {

    private Integer branchId;

    private String street;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private BigDecimal quantity;
}