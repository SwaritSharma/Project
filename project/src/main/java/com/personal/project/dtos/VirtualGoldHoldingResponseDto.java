package com.personal.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VirtualGoldHoldingResponseDto {

    private Integer holdingId;

    private String vendorName;

    private String branchCity;

    private String branchState;

    private String quantity;

    private String createdAt;
}