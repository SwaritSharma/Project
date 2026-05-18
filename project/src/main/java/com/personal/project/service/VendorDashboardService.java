package com.personal.project.service;

import com.personal.project.dto.AddBranchRequest;
import com.personal.project.dto.EditVendorProfileRequest;
import com.personal.project.dto.TransactionDTO;
import com.personal.project.dto.VendorBranchDTO;
import com.personal.project.dto.VendorDashboardDTO;

import java.math.BigDecimal;
import java.util.List;

public interface VendorDashboardService {

    VendorDashboardDTO getDashboard(Integer vendorId);

    VendorDashboardDTO updateProfile(Integer vendorId, EditVendorProfileRequest request);

    List<VendorBranchDTO> getBranches(Integer vendorId);

    VendorBranchDTO addBranch(Integer vendorId, AddBranchRequest request);

    void deleteBranch(Integer vendorId, Integer branchId);

    List<TransactionDTO> getTransactions(Integer vendorId);

    void addGoldToBranch(Integer vendorId, Integer branchId, BigDecimal quantity);
}
