package com.personal.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VendorBranchDTO {
    @JsonProperty("branch_id")
    private Integer branchId;

    private BigDecimal quantity;

    private HoldingDTO.AddressDTO address;
}
