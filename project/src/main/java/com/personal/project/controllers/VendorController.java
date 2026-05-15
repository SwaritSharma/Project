package com.personal.project.controllers;

import com.personal.project.dtos.VendorBranchResponseDto;
import com.personal.project.dtos.VendorDashboardResponseDto;
import com.personal.project.services.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    public ResponseEntity<Page<VendorDashboardResponseDto>>
    getAllVendors(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                vendorService.getAllVendors(
                        pageable
                )
        );
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<VendorDashboardResponseDto>
    getVendorById(
            @PathVariable
            Integer vendorId
    ) {

        return ResponseEntity.ok(
                vendorService.getVendorById(
                        vendorId
                )
        );
    }

    @GetMapping("/{vendorId}/branches")
    public ResponseEntity<Page<VendorBranchResponseDto>>
    getVendorBranches(
            @PathVariable
            Integer vendorId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                vendorService.getVendorBranches(
                        vendorId,
                        pageable
                )
        );
    }

    @GetMapping("/{vendorId}/branches/state/{state}")
    public ResponseEntity<Page<VendorBranchResponseDto>>
    getVendorBranchesByState(
            @PathVariable
            Integer vendorId,

            @PathVariable
            String state,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                vendorService.getVendorBranchesByState(
                        vendorId,
                        state,
                        pageable
                )
        );
    }

    @GetMapping("/{vendorId}/branches/country/{country}")
    public ResponseEntity<Page<VendorBranchResponseDto>>
    getVendorBranchesByCountry(
            @PathVariable
            Integer vendorId,

            @PathVariable
            String country,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                vendorService.getVendorBranchesByCountry(
                        vendorId,
                        country,
                        pageable
                )
        );
    }

    @GetMapping("/{vendorId}/branches/sort/quantity-desc")
    public ResponseEntity<Page<VendorBranchResponseDto>>
    getVendorBranchesSortedByQuantityDesc(
            @PathVariable
            Integer vendorId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                vendorService
                        .getVendorBranchesSortedByQuantityDesc(
                                vendorId,
                                pageable
                        )
        );
    }

    @GetMapping("/{vendorId}/branches/sort/quantity-asc")
    public ResponseEntity<Page<VendorBranchResponseDto>>
    getVendorBranchesSortedByQuantityAsc(
            @PathVariable
            Integer vendorId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                vendorService
                        .getVendorBranchesSortedByQuantityAsc(
                                vendorId,
                                pageable
                        )
        );
    }

    @GetMapping("/{vendorId}/dashboard")
    public ResponseEntity<VendorDashboardResponseDto>
    getVendorDashboard(
            @PathVariable
            Integer vendorId
    ) {

        return ResponseEntity.ok(
                vendorService.getVendorDashboard(
                        vendorId
                )
        );
    }
}