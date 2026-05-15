package com.personal.project.repositories;

import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorRepository
        extends JpaRepository<Vendor, Integer> {

    boolean existsByVendorName(
            String vendorName
    );

    Optional<Vendor> findByVendorName(
            String vendorName
    );

    List<TransactionHistory>
    findTop5ByBranchVendorVendorIdOrderByCreatedAtDesc(
            Integer vendorId
    );
}