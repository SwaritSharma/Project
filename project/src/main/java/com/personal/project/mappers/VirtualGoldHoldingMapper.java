package com.personal.project.mappers;

import com.personal.project.dtos.VirtualGoldHoldingResponseDto;
import com.personal.project.entity.VirtualGoldHolding;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VirtualGoldHoldingMapper {

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
    VirtualGoldHoldingResponseDto
    toVirtualGoldHoldingResponseDto(
            VirtualGoldHolding virtualGoldHolding
    );

    List<VirtualGoldHoldingResponseDto>
    toVirtualGoldHoldingResponseDtos(
            List<VirtualGoldHolding> virtualGoldHoldings
    );
}