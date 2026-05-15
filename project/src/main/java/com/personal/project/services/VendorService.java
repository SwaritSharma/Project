package com.personal.project.services;

import com.personal.project.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorService {

    VendorDashboardResponseDto registerVendor(
            VendorRegistrationRequestDto dto
    );

    VendorDashboardResponseDto authenticateVendor(
            VendorLoginRequestDto dto
    );

    VendorDashboardResponseDto getVendorDashboard(
            Integer vendorId
    );

    VendorBranchResponseDto registerVendorBranch(
            Integer vendorId,
            RegisterVendorBranchRequestDto dto
    );

    VendorBranchResponseDto addGoldToBranch(
            Integer branchId,
            AddGoldToBranchRequestDto dto
    );

    Page<VendorBranchResponseDto> getVendorBranches(
            Integer vendorId,
            Pageable pageable
    );

    Page<VendorBranchResponseDto>
    getVendorBranchesByState(
            Integer vendorId,
            String state,
            Pageable pageable
    );

    Page<VendorBranchResponseDto>
    getVendorBranchesByCountry(
            Integer vendorId,
            String country,
            Pageable pageable
    );

    Page<VendorBranchResponseDto>
    getVendorBranchesSortedByQuantityAsc(
            Integer vendorId,
            Pageable pageable
    );

    Page<VendorBranchResponseDto>
    getVendorBranchesSortedByQuantityDesc(
            Integer vendorId,
            Pageable pageable
    );

    Page<VendorDashboardResponseDto>
    getAllVendors(
            Pageable pageable
    );

    VendorDashboardResponseDto
    getVendorById(
            Integer vendorId
    );
}