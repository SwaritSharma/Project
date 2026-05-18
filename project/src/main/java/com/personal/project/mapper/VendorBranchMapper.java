package com.personal.project.mapper;

import com.personal.project.dto.VendorBranchDTO;
import com.personal.project.entity.Address;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface VendorBranchMapper {

    VendorBranchDTO toDto(VendorBranch branch);

    List<VendorBranchDTO> toDtoList(List<VendorBranch> branches);

    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "vendor", source = "vendor")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "createdAt", source = "createdAt")
    VendorBranch toEntity(Vendor vendor, Address address, BigDecimal quantity, LocalDateTime createdAt);
}
