package com.personal.project.controller;

import com.personal.project.dto.VendorDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorRepository vendorRepository;
    private final GoldPriceService goldPriceService;

    @GetMapping
    public List<VendorDTO> getVendors() {
        return vendorRepository.findAll().stream().map(v -> {
            VendorDTO dto = new VendorDTO();
            dto.setVendorId(v.getVendorId());
            dto.setVendorName(v.getVendorName());
            dto.setCurrentGoldPrice(v.getCurrentGoldPrice() != null ? v.getCurrentGoldPrice() : goldPriceService.getCurrentPrice().getPrice());
            return dto;
        }).collect(Collectors.toList());
    }

    private final VendorDashboardService vendorDashboardService;

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<VendorDashboardDTO> getDashboard(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(vendorDashboardService.getDashboard(id));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<VendorDashboardDTO> updateProfile(@PathVariable("id") Integer id, @RequestBody EditVendorProfileRequest request) {
        return ResponseEntity.ok(vendorDashboardService.updateProfile(id, request));
    }

    @GetMapping("/{id}/branches")
    public ResponseEntity<List<VendorBranchDTO>> getBranches(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(vendorDashboardService.getBranches(id));
    }

    @PostMapping("/{id}/branches")
    public ResponseEntity<VendorBranchDTO> addBranch(@PathVariable("id") Integer id, @Valid @RequestBody AddBranchRequest request) {
        return ResponseEntity.ok(vendorDashboardService.addBranch(id, request));
    }

    @DeleteMapping("/{id}/branches/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable("id") Integer id, @PathVariable("branchId") Integer branchId) {
        vendorDashboardService.deleteBranch(id, branchId);
        return ResponseEntity.ok().build();
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
