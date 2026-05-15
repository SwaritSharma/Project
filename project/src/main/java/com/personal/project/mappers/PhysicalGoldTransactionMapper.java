package com.personal.project.mappers;

import com.personal.project.dtos.ConvertVirtualToPhysicalResponseDto;
import com.personal.project.dtos.PhysicalGoldTransactionResponseDto;
import com.personal.project.entity.PhysicalGoldTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhysicalGoldTransactionMapper {

    @Mapping(
            target = "vendorName",
            source = "branch.vendor.vendorName"
    )
    @Mapping(
            target = "branchCity",
            source = "branch.address.city"
    )
    @Mapping(
            target = "branchState",
            source = "branch.address.state"
    )
    @Mapping(
            target = "deliveryStreet",
            source = "deliveryAddress.street"
    )
    @Mapping(
            target = "deliveryCity",
            source = "deliveryAddress.city"
    )
    @Mapping(
            target = "deliveryState",
            source = "deliveryAddress.state"
    )
    @Mapping(
            target = "deliveryPostalCode",
            source = "deliveryAddress.postalCode"
    )
    @Mapping(
            target = "deliveryCountry",
            source = "deliveryAddress.country"
    )
    PhysicalGoldTransactionResponseDto
    toPhysicalGoldTransactionResponseDto(
            PhysicalGoldTransaction physicalGoldTransaction
    );

    List<PhysicalGoldTransactionResponseDto>
    toPhysicalGoldTransactionResponseDtos(
            List<PhysicalGoldTransaction> physicalGoldTransactions
    );

    @Mapping(
            target = "vendorName",
            source = "branch.vendor.vendorName"
    )
    @Mapping(
            target = "branchCity",
            source = "branch.address.city"
    )
    @Mapping(
            target = "branchState",
            source = "branch.address.state"
    )
    @Mapping(
            target = "deliveryStreet",
            source = "deliveryAddress.street"
    )
    @Mapping(
            target = "deliveryCity",
            source = "deliveryAddress.city"
    )
    @Mapping(
            target = "deliveryState",
            source = "deliveryAddress.state"
    )
    @Mapping(
            target = "deliveryPostalCode",
            source = "deliveryAddress.postalCode"
    )
    @Mapping(
            target = "deliveryCountry",
            source = "deliveryAddress.country"
    )
    ConvertVirtualToPhysicalResponseDto
    toConvertVirtualToPhysicalResponseDto(
            PhysicalGoldTransaction physicalGoldTransaction
    );
}