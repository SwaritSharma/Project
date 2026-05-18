package com.personal.project.mapper;

import com.personal.project.dto.EditVendorProfileRequest;
import com.personal.project.dto.VendorDTO;
import com.personal.project.dto.VendorRegisterRequest;
import com.personal.project.entity.Vendor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VendorMapper {

    VendorDTO toDto(Vendor vendor);

    List<VendorDTO> toDtoList(List<Vendor> vendors);

    default VendorDTO toDto(Vendor vendor, BigDecimal fallbackGoldPrice) {
        VendorDTO dto = toDto(vendor);
        if (dto != null && dto.getCurrentGoldPrice() == null) {
            dto.setCurrentGoldPrice(fallbackGoldPrice);
        }
        return dto;
    }

    @Mapping(target = "vendorId", ignore = true)
    @Mapping(target = "totalGoldQuantity", ignore = true)
    @Mapping(target = "currentGoldPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    Vendor toEntity(VendorRegisterRequest request);

    @Mapping(target = "vendorId", ignore = true)
    @Mapping(target = "vendorName", ignore = true)
    @Mapping(target = "totalGoldQuantity", ignore = true)
    @Mapping(target = "currentGoldPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfile(EditVendorProfileRequest request, @MappingTarget Vendor vendor);
}
