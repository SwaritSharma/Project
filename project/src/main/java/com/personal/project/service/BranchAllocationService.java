package com.personal.project.service;

import com.personal.project.entity.VendorBranch;

import java.math.BigDecimal;

public interface BranchAllocationService {

    VendorBranch allocateBranch(
            Integer vendorId,
            Integer deliveryAddressId,
            BigDecimal requiredQuantity
    );
}