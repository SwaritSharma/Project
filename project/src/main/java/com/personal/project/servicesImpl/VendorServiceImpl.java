package com.personal.project.servicesImpl;

import com.personal.project.dtos.*;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import com.personal.project.exceptions.DuplicateResourceException;
import com.personal.project.exceptions.InvalidCredentialsException;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.VendorMapper;
import com.personal.project.repositories.VendorBranchRepository;
import com.personal.project.repositories.VendorRepository;
import com.personal.project.services.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl
        implements VendorService {

    private final VendorRepository vendorRepository;

    private final VendorBranchRepository vendorBranchRepository;

    private final VendorMapper vendorMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public VendorDashboardResponseDto registerVendor(
            VendorRegistrationRequestDto dto
    ) {

        boolean exists =
                vendorRepository.existsByVendorName(
                        dto.getVendorName()
                );

        if (exists) {
            throw new DuplicateResourceException(
                    "Vendor name already exists"
            );
        }

        Vendor vendor =
                vendorMapper.toVendor(dto);

        vendor.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        vendor.setTotalGoldQuantity(
                BigDecimal.ZERO
        );

        vendor.setCurrentGoldPrice(
                BigDecimal.valueOf(5700)
        );

        Vendor savedVendor =
                vendorRepository.save(vendor);

        VendorDashboardResponseDto response =
                vendorMapper.toVendorDashboardResponseDto(
                        savedVendor
                );

        response.setTotalBranches(0L);

        return response;
    }

    @Override
    public VendorDashboardResponseDto authenticateVendor(
            VendorLoginRequestDto dto
    ) {

        Vendor vendor =
                vendorRepository.findByVendorName(
                                dto.getVendorName()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vendor not found"
                                )
                        );

        boolean matches =
                passwordEncoder.matches(
                        dto.getPassword(),
                        vendor.getPassword()
                );

        if (!matches) {
            throw new InvalidCredentialsException(
                    "Invalid vendor name or password"
            );
        }

        VendorDashboardResponseDto response =
                vendorMapper.toVendorDashboardResponseDto(
                        vendor
                );

        Long totalBranches =
                vendorBranchRepository.countByVendorVendorId(
                        vendor.getVendorId()
                );

        response.setTotalBranches(
                totalBranches
        );

        return response;
    }

    @Override
    public VendorDashboardResponseDto getVendorDashboard(
            Integer vendorId
    ) {

        Vendor vendor =
                vendorRepository.findById(vendorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vendor not found"
                                )
                        );

        VendorDashboardResponseDto response =
                vendorMapper.toVendorDashboardResponseDto(
                        vendor
                );

        Long totalBranches =
                vendorBranchRepository.countByVendorVendorId(
                        vendorId
                );

        response.setTotalBranches(
                totalBranches
        );

        return response;
    }

    @Override
    public VendorBranchResponseDto registerVendorBranch(
            Integer vendorId,
            RegisterVendorBranchRequestDto dto
    ) {

        Vendor vendor =
                vendorRepository.findById(vendorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vendor not found"
                                )
                        );

        VendorBranch vendorBranch =
                vendorMapper.toVendorBranch(dto);

        vendorBranch.setVendor(vendor);

        VendorBranch savedBranch =
                vendorBranchRepository.save(
                        vendorBranch
                );

        vendor.setTotalGoldQuantity(
                vendor.getTotalGoldQuantity()
                        .add(dto.getQuantity())
        );

        vendorRepository.save(vendor);

        return vendorMapper.toVendorBranchResponseDto(
                savedBranch
        );
    }

    @Override
    public VendorBranchResponseDto addGoldToBranch(
            Integer branchId,
            AddGoldToBranchRequestDto dto
    ) {

        VendorBranch vendorBranch =
                vendorBranchRepository.findById(branchId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Branch not found"
                                )
                        );

        vendorBranch.setQuantity(
                vendorBranch.getQuantity()
                        .add(dto.getGoldQuantity())
        );

        VendorBranch updatedBranch =
                vendorBranchRepository.save(
                        vendorBranch
                );

        Vendor vendor =
                vendorBranch.getVendor();

        vendor.setTotalGoldQuantity(
                vendor.getTotalGoldQuantity()
                        .add(dto.getGoldQuantity())
        );

        vendorRepository.save(vendor);

        return vendorMapper.toVendorBranchResponseDto(
                updatedBranch
        );
    }

    @Override
    public Page<VendorBranchResponseDto>
    getVendorBranches(
            Integer vendorId,
            Pageable pageable
    ) {

        return vendorBranchRepository
                .findByVendorVendorId(
                        vendorId,
                        pageable
                )
                .map(
                        vendorMapper::toVendorBranchResponseDto
                );
    }

    @Override
    public Page<VendorBranchResponseDto>
    getVendorBranchesByState(
            Integer vendorId,
            String state,
            Pageable pageable
    ) {

        return vendorBranchRepository
                .findByVendorVendorIdAndAddressState(
                        vendorId,
                        state,
                        pageable
                )
                .map(
                        vendorMapper::toVendorBranchResponseDto
                );
    }

    @Override
    public Page<VendorBranchResponseDto>
    getVendorBranchesByCountry(
            Integer vendorId,
            String country,
            Pageable pageable
    ) {

        return vendorBranchRepository
                .findByVendorVendorIdAndAddressCountry(
                        vendorId,
                        country,
                        pageable
                )
                .map(
                        vendorMapper::toVendorBranchResponseDto
                );
    }

    @Override
    public Page<VendorBranchResponseDto>
    getVendorBranchesSortedByQuantityAsc(
            Integer vendorId,
            Pageable pageable
    ) {

        return vendorBranchRepository
                .findByVendorVendorIdOrderByQuantityAsc(
                        vendorId,
                        pageable
                )
                .map(
                        vendorMapper::toVendorBranchResponseDto
                );
    }

    @Override
    public Page<VendorBranchResponseDto>
    getVendorBranchesSortedByQuantityDesc(
            Integer vendorId,
            Pageable pageable
    ) {

        return vendorBranchRepository
                .findByVendorVendorIdOrderByQuantityDesc(
                        vendorId,
                        pageable
                )
                .map(
                        vendorMapper::toVendorBranchResponseDto
                );
    }
    @Override
    public Page<VendorDashboardResponseDto>
    getAllVendors(
            Pageable pageable
    ) {

        return vendorRepository
                .findAll(pageable)
                .map(
                        vendorMapper
                                ::toVendorDashboardResponseDto
                );
    }

    @Override
    public VendorDashboardResponseDto
    getVendorById(
            Integer vendorId
    ) {

        Vendor vendor =
                vendorRepository.findById(vendorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vendor not found"
                                )
                        );

        return vendorMapper
                .toVendorDashboardResponseDto(
                        vendor
                );
    }
}