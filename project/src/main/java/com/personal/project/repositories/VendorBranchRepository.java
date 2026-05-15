package com.personal.project.repositories;

import com.personal.project.entity.VendorBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface VendorBranchRepository
        extends JpaRepository<VendorBranch, Integer> {

    long countByVendorVendorId(
            Integer vendorId
    );

    Page<VendorBranch>
    findByVendorVendorIdAndAddressState(
            Integer vendorId,
            String state,
            Pageable pageable
    );

    Page<VendorBranch>
    findByVendorVendorIdAndAddressCountry(
            Integer vendorId,
            String country,
            Pageable pageable
    );

    Page<VendorBranch>
    findByVendorVendorIdOrderByQuantityAsc(
            Integer vendorId,
            Pageable pageable
    );

    Page<VendorBranch>
    findByVendorVendorIdOrderByQuantityDesc(
            Integer vendorId,
            Pageable pageable
    );

    Optional<VendorBranch>
    findFirstByVendorVendorIdAndAddressPostalCode(
            Integer vendorId,
            String postalCode
    );

    Page<VendorBranch>
    findByVendorVendorId(
            Integer vendorId,
            Pageable pageable
    );

    Optional<VendorBranch>
    findFirstByVendorVendorIdOrderByQuantityAsc(
            Integer vendorId
    );

    Optional<VendorBranch>
    findFirstByVendorVendorIdAndAddressPostalCodeAndQuantityGreaterThanEqualOrderByQuantityDesc(
            Integer vendorId,
            String postalCode,
            BigDecimal quantity
    );

    Optional<VendorBranch>
    findFirstByVendorVendorIdAndAddressPostalCodeOrderByQuantityDesc(
            Integer vendorId,
            String postalCode
    );

    Optional<VendorBranch>
    findFirstByVendorVendorIdAndAddressPostalCodeAndQuantityGreaterThanEqual(
            Integer vendorId,
            String postalCode,
            BigDecimal quantity
    );
}