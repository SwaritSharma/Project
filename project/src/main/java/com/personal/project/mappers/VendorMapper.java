package com.personal.project.mappers;

import com.personal.project.dtos.*;
import com.personal.project.entity.Address;
import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VendorMapper {

    @Mapping(target = "vendorId", ignore = true)
    @Mapping(target = "totalGoldQuantity", ignore = true)
    @Mapping(target = "currentGoldPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "branches", ignore = true)
    Vendor toVendor(
            VendorRegistrationRequestDto dto
    );

    VendorDashboardResponseDto
    toVendorDashboardResponseDto(
            Vendor vendor
    );

    List<VendorLatestTransactionDto>
    toVendorLatestTransactionDtos(
            List<TransactionHistory> transactions
    );

    @Mapping(
            target = "branchCity",
            source = "branch.address.city"
    )
    @Mapping(
            target = "branchState",
            source = "branch.address.state"
    )
    VendorLatestTransactionDto
    toVendorLatestTransactionDto(
            TransactionHistory transactionHistory
    );

    @Mapping(
            target = "street",
            source = "address.street"
    )
    @Mapping(
            target = "city",
            source = "address.city"
    )
    @Mapping(
            target = "state",
            source = "address.state"
    )
    @Mapping(
            target = "postalCode",
            source = "address.postalCode"
    )
    @Mapping(
            target = "country",
            source = "address.country"
    )
    VendorBranchResponseDto
    toVendorBranchResponseDto(
            VendorBranch vendorBranch
    );

    List<VendorBranchResponseDto>
    toVendorBranchResponseDtos(
            List<VendorBranch> vendorBranches
    );

    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "vendor", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "holdings", ignore = true)
    @Mapping(target = "physicalGoldTransactions", ignore = true)
    @Mapping(
            target = "address",
            expression = "java(mapAddress(dto))"
    )
    VendorBranch toVendorBranch(
            RegisterVendorBranchRequestDto dto
    );

    default Address mapAddress(
            RegisterVendorBranchRequestDto dto
    ) {
        return Address.builder()
                .street(dto.getStreet())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .build();
    }
}