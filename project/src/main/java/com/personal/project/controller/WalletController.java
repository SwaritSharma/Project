package com.personal.project.controller;

import com.personal.project.dto.WalletTopupRequest;
import com.personal.project.entity.User;
import com.personal.project.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService
            walletService;

    public WalletController(
            WalletService walletService
    ) {

        this.walletService =
                walletService;
    }

    @PostMapping(
            "/topup"
    )
    public ResponseEntity<User>
    topupWallet(
            @Valid
            @RequestBody
            WalletTopupRequest request
    ) {

        User updatedUser =
                walletService
                        .topupWallet(request);

        return ResponseEntity.ok(
                updatedUser
        );
    }
}