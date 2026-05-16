package com.personal.project.controller;

import com.personal.project.dto.DashboardDTO;
import com.personal.project.dto.HoldingDTO;
import com.personal.project.dto.TransactionDTO;
import com.personal.project.dto.PhysicalGoldDTO;
import com.personal.project.dto.EditProfileRequest;
import com.personal.project.service.DashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{id}/dashboard")
    public DashboardDTO getDashboard(@PathVariable("id") Integer id) {
        return dashboardService.getDashboard(id);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<Void> updateProfile(@PathVariable("id") Integer id, @Valid @RequestBody EditProfileRequest request) {
        dashboardService.updateProfile(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/holdings")
    public List<HoldingDTO> getHoldings(@PathVariable("id") Integer id) {
        return dashboardService.getHoldings(id);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionDTO> getTransactions(@PathVariable("id") Integer id) {
        return dashboardService.getTransactions(id);
    }

    @GetMapping("/{id}/addresses")
    public List<HoldingDTO.AddressDTO> getAddresses(@PathVariable("id") Integer id) {
        return dashboardService.getAddresses(id);
    }

    @GetMapping("/{id}/physical-gold")
    public List<PhysicalGoldDTO> getPhysicalGold(@PathVariable("id") Integer id) {
        return dashboardService.getPhysicalGold(id);
    }

    @GetMapping("/{id}/payments")
    public List<com.personal.project.dto.PaymentDTO> getPayments(@PathVariable("id") Integer id) {
        return dashboardService.getPayments(id);
    }
}
