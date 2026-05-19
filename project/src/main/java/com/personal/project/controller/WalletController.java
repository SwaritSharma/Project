package com.personal.project.controller;

import com.personal.project.dto.WalletTopupRequest;
import com.personal.project.dto.UserDTO;
import com.personal.project.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/topup")
    public ResponseEntity<UserDTO> topupWallet(@Valid @RequestBody WalletTopupRequest request) {
        return ResponseEntity.ok(walletService.topupWallet(request));
    }
}
