package com.personal.project.controller;

import com.personal.project.dto.VendorDTO;
import com.personal.project.mapper.VendorMapper;
import com.personal.project.repository.VendorRepository;
import com.personal.project.service.GoldPriceService;
import lombok.RequiredArgsConstructor;
import com.personal.project.dto.AddBranchRequest;
import com.personal.project.dto.VendorBranchDTO;
import com.personal.project.dto.VendorDashboardDTO;
import com.personal.project.dto.EditVendorProfileRequest;
import com.personal.project.dto.TransactionDTO;
import com.personal.project.dto.AddGoldRequest;
import com.personal.project.service.VendorDashboardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import static com.personal.project.config.RedisCacheConfig.VENDORS_CACHE;

@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorRepository vendorRepository;
    private final GoldPriceService goldPriceService;
    private final VendorMapper vendorMapper;

    @GetMapping
    @Cacheable(cacheNames = VENDORS_CACHE, key = "'all'")
    public List<VendorDTO> getVendors() {
        return vendorRepository.findAll().stream()
                .map(vendor -> vendorMapper.toDto(
                        vendor,
                        vendor.getCurrentGoldPrice() != null
                                ? vendor.getCurrentGoldPrice()
                                : goldPriceService.getCurrentPrice().getPrice()
                ))
                .toList();
    }

    private final VendorDashboardService vendorDashboardService;

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<VendorDashboardDTO> getDashboard(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(vendorDashboardService.getDashboard(id));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<VendorDashboardDTO> updateProfile(@PathVariable("id") Integer id, @Valid @RequestBody EditVendorProfileRequest request) {
        return ResponseEntity.ok(vendorDashboardService.updateProfile(id, request));
    }

    @GetMapping("/{id}/branches")
    public ResponseEntity<List<VendorBranchDTO>> getBranches(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(vendorDashboardService.getBranches(id));
    }

    @PostMapping("/{id}/branches")
    public ResponseEntity<VendorBranchDTO> addBranch(@PathVariable("id") Integer id, @Valid @RequestBody AddBranchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorDashboardService.addBranch(id, request));
    }

    @DeleteMapping("/{id}/branches/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable("id") Integer id, @PathVariable("branchId") Integer branchId) {
        vendorDashboardService.deleteBranch(id, branchId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(vendorDashboardService.getTransactions(id));
    }

    @PostMapping("/{id}/add-gold")
    public ResponseEntity<Void> addGold(@PathVariable("id") Integer id, @Valid @RequestBody AddGoldRequest request) {
        vendorDashboardService.addGoldToBranch(id, request.getBranchId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }
}
