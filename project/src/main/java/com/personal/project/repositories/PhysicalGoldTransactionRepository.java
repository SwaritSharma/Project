package com.personal.project.repositories;

import com.personal.project.entity.PhysicalGoldTransaction;
import com.personal.project.entity.VendorBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface PhysicalGoldTransactionRepository
        extends JpaRepository<PhysicalGoldTransaction, Integer> {


    Page<PhysicalGoldTransaction>
    findByUserUserId(
            Integer userId,
            Pageable pageable
    );

    Page<PhysicalGoldTransaction>
    findByUserUserIdAndBranchBranchId(
            Integer userId,
            Integer branchId,
            Pageable pageable
    );

    Page<PhysicalGoldTransaction>
    findByUserUserIdOrderByCreatedAtDesc(
            Integer userId,
            Pageable pageable
    );

    Page<PhysicalGoldTransaction>
    findByUserUserIdOrderByCreatedAtAsc(
            Integer userId,
            Pageable pageable
    );

    Page<PhysicalGoldTransaction>
    findByUserUserIdOrderByQuantityDesc(
            Integer userId,
            Pageable pageable
    );

    Page<PhysicalGoldTransaction>
    findByUserUserIdOrderByQuantityAsc(
            Integer userId,
            Pageable pageable
    );
}