package com.personal.project.mapper;

import com.personal.project.dto.AddBranchRequest;
import com.personal.project.dto.EditProfileRequest;
import com.personal.project.dto.HoldingDTO;
import com.personal.project.dto.RegisterRequest;
import com.personal.project.dto.VendorRegisterRequest;
import com.personal.project.entity.Address;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    HoldingDTO.AddressDTO toDto(Address address);

    @Mapping(target = "addressId", ignore = true)
    Address toEntity(AddBranchRequest request);

    @Mapping(target = "addressId", ignore = true)
    Address toEntity(RegisterRequest request);

    @Mapping(target = "addressId", ignore = true)
    Address toEntity(VendorRegisterRequest request);

    @Mapping(target = "addressId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromRegister(RegisterRequest request, @MappingTarget Address address);

    @Mapping(target = "addressId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromProfile(EditProfileRequest request, @MappingTarget Address address);
}
