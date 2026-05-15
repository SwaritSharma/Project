package com.personal.project.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDashboardResponseDto {

    private Integer vendorId;

    private String vendorName;

    private String description;

    private String contactPersonName;

    private String contactEmail;

    private String contactPhone;

    private String websiteUrl;

    private BigDecimal totalGoldQuantity;

    private BigDecimal currentGoldPrice;

    private Long totalBranches;
}