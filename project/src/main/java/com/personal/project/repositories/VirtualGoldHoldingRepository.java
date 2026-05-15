package com.personal.project.repositories;

import com.personal.project.entity.VendorBranch;
import com.personal.project.entity.VirtualGoldHolding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface VirtualGoldHoldingRepository
        extends JpaRepository<VirtualGoldHolding, Integer> {



    Optional<VirtualGoldHolding>
    findByUserUserIdAndBranchBranchId(
            Integer userId,
            Integer branchId
    );


    Page<VirtualGoldHolding>
    findByUserUserId(
            Integer userId,
            Pageable pageable
    );

    Page<VirtualGoldHolding>
    findByUserUserIdOrderByQuantityDesc(
            Integer userId,
            Pageable pageable
    );

    Page<VirtualGoldHolding>
    findByUserUserIdOrderByQuantityAsc(
            Integer userId,
            Pageable pageable
    );
}