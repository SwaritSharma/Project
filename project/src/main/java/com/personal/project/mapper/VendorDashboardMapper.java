package com.personal.project.mapper;

import com.personal.project.dto.VendorDashboardDTO;
import com.personal.project.entity.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface VendorDashboardMapper {

    @Mapping(target = "totalBranches", source = "totalBranches")
    @Mapping(target = "totalGoldQuantity", source = "totalInventory")
    @Mapping(target = "totalSoldQuantity", source = "totalSoldQuantity")
    @Mapping(target = "currentGoldPrice", source = "currentGoldPrice")
    VendorDashboardDTO toDashboard(
            Vendor vendor,
            Integer totalBranches,
            BigDecimal totalInventory,
            BigDecimal totalSoldQuantity,
            BigDecimal currentGoldPrice
    );
}
