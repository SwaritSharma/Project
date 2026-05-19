package com.personal.project.service.impl;

import com.personal.project.service.VendorDashboardService;
import com.personal.project.service.GoldPriceService;

import com.personal.project.dto.AddBranchRequest;
import com.personal.project.dto.TransactionDTO;
import com.personal.project.dto.VendorBranchDTO;
import com.personal.project.dto.VendorDashboardDTO;
import com.personal.project.dto.EditVendorProfileRequest;
import com.personal.project.entity.Address;
import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import com.personal.project.exception.BranchAllocationException;
import com.personal.project.exception.InvalidQuantityException;
import com.personal.project.exception.VendorNotFoundException;
import com.personal.project.mapper.AddressMapper;
import com.personal.project.mapper.TransactionMapper;
import com.personal.project.mapper.VendorBranchMapper;
import com.personal.project.mapper.VendorDashboardMapper;
import com.personal.project.mapper.VendorMapper;
import com.personal.project.repository.AddressRepository;
import com.personal.project.repository.TransactionHistoryRepository;
import com.personal.project.repository.VendorBranchRepository;
import com.personal.project.repository.VendorRepository;
import com.personal.project.repository.VirtualGoldHoldingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

import static com.personal.project.config.RedisCacheConfig.VENDOR_BRANCHES_CACHE;
import static com.personal.project.config.RedisCacheConfig.VENDOR_DASHBOARD_CACHE;
import static com.personal.project.config.RedisCacheConfig.VENDOR_TRANSACTIONS_CACHE;
import static com.personal.project.config.RedisCacheConfig.VENDORS_CACHE;

@Service
@RequiredArgsConstructor
public class VendorDashboardServiceImpl implements VendorDashboardService {

    private final VendorRepository vendorRepository;
    private final VendorBranchRepository vendorBranchRepository;
    private final TransactionHistoryRepository transactionRepository;
    private final AddressRepository addressRepository;
    private final GoldPriceService goldPriceService;
    private final VendorMapper vendorMapper;
    private final AddressMapper addressMapper;
    private final VendorBranchMapper vendorBranchMapper;
    private final TransactionMapper transactionMapper;
    private final VendorDashboardMapper vendorDashboardMapper;
    private final VirtualGoldHoldingRepository holdingRepository;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = VENDOR_DASHBOARD_CACHE, key = "#vendorId")
    public VendorDashboardDTO getDashboard(Integer vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found"));
        
        List<VendorBranch> branches = vendorBranchRepository.findByVendorVendorId(vendorId);
        BigDecimal totalInventory = branches.stream()
                .map(branch -> branch.getQuantity() != null ? branch.getQuantity() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSold = transactionRepository.sumQuantityByVendorIdAndTransactionTypeAndTransactionStatus(vendorId); 
        
        return vendorDashboardMapper.toDashboard(
                vendor,
                branches.size(),
                totalInventory,
                totalSold,
                goldPriceService.getCurrentPrice().getPrice()
        );
    }

    @Transactional
    @CacheEvict(cacheNames = {
            VENDOR_DASHBOARD_CACHE,
            VENDORS_CACHE
    }, key = "#vendorId")
    public VendorDashboardDTO updateProfile(Integer vendorId, EditVendorProfileRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found"));
        vendorMapper.updateProfile(request, vendor);
        vendorRepository.save(vendor);
        return getDashboard(vendorId);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = VENDOR_BRANCHES_CACHE, key = "#vendorId")
    public List<VendorBranchDTO> getBranches(Integer vendorId) {
        return vendorBranchMapper.toDtoList(vendorBranchRepository.findByVendorVendorId(vendorId));
    }

    @Transactional
    @CacheEvict(cacheNames = {
            VENDOR_DASHBOARD_CACHE,
            VENDOR_BRANCHES_CACHE,
            VENDOR_TRANSACTIONS_CACHE,
            VENDORS_CACHE
    }, key = "#vendorId")
    public VendorBranchDTO addBranch(Integer vendorId, AddBranchRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("Vendor not found"));
        
        Address address = addressMapper.toEntity(request);
        address = addressRepository.save(address);

        VendorBranch branch = vendorBranchMapper.toEntity(vendor, address, request.getInitialQuantity(), LocalDateTime.now());
        branch = vendorBranchRepository.save(branch);

        if (request.getInitialQuantity() != null && request.getInitialQuantity().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentVendorQty = vendor.getTotalGoldQuantity() != null ? vendor.getTotalGoldQuantity() : BigDecimal.ZERO;
            vendor.setTotalGoldQuantity(currentVendorQty.add(request.getInitialQuantity()));
            vendorRepository.save(vendor);

            TransactionHistory th = transactionMapper.toEntity(
                    null,
                    branch,
                    request.getInitialQuantity(),
                    BigDecimal.ZERO,
                    "Add Inventory",
                    "Success",
                    LocalDateTime.now()
            );
            transactionRepository.save(th);
        }

        return vendorBranchMapper.toDto(branch);
    }

    @Transactional
    @CacheEvict(cacheNames = {
            VENDOR_DASHBOARD_CACHE,
            VENDOR_BRANCHES_CACHE
    }, key = "#vendorId")
    public void deleteBranch(Integer vendorId, Integer branchId) {
        VendorBranch branch = vendorBranchRepository.findById(branchId).orElseThrow(() -> new BranchAllocationException("Branch not found"));
        if (branch.getVendor() == null || !branch.getVendor().getVendorId().equals(vendorId)) {
            throw new BranchAllocationException("Branch does not belong to this vendor");
        }
        if (transactionRepository.existsByBranchBranchId(branchId) || holdingRepository.existsByBranchBranchId(branchId)) {
            throw new BranchAllocationException("Cannot delete branch because it has active holdings or transaction history");
        }
        vendorBranchRepository.delete(branch);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = VENDOR_TRANSACTIONS_CACHE, key = "#vendorId")
    public List<TransactionDTO> getTransactions(Integer vendorId) {
        return transactionMapper.toDtoList(
                transactionRepository.findByBranchVendorVendorIdOrderByCreatedAtDesc(vendorId, PageRequest.of(0, 100)).getContent()
        );
    }

    @Transactional
    @CacheEvict(cacheNames = {
            VENDOR_DASHBOARD_CACHE,
            VENDOR_BRANCHES_CACHE,
            VENDOR_TRANSACTIONS_CACHE,
            VENDORS_CACHE
    }, key = "#vendorId")
    public void addGoldToBranch(Integer vendorId, Integer branchId, BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidQuantityException("Quantity must be positive");
        }

        VendorBranch branch = vendorBranchRepository.findById(branchId).orElseThrow(() -> new BranchAllocationException("Branch not found"));
        if (branch.getVendor() == null || !branch.getVendor().getVendorId().equals(vendorId)) {
            throw new BranchAllocationException("Branch does not belong to this vendor");
        }
        
        BigDecimal currentBranchQty = branch.getQuantity() != null ? branch.getQuantity() : BigDecimal.ZERO;
        branch.setQuantity(currentBranchQty.add(quantity));
        vendorBranchRepository.save(branch);
        
        Vendor vendor = branch.getVendor();
        BigDecimal currentVendorQty = vendor.getTotalGoldQuantity() != null ? vendor.getTotalGoldQuantity() : BigDecimal.ZERO;
        vendor.setTotalGoldQuantity(currentVendorQty.add(quantity));
        vendorRepository.save(vendor);
        
        TransactionHistory th = transactionMapper.toEntity(
                null,
                branch,
                quantity,
                BigDecimal.ZERO,
                "Add Inventory",
                "Success",
                LocalDateTime.now()
        );
        transactionRepository.save(th);
    }
}
