package com.personal.project.controller;

import com.personal.project.dto.BuyVirtualGoldRequest;
import com.personal.project.dto.SellVirtualGoldRequest;
import com.personal.project.service.VirtualGoldService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/virtual-gold")
@RequiredArgsConstructor
public class VirtualGoldController {

    private final VirtualGoldService virtualGoldService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyVirtualGold(@Valid @RequestBody BuyVirtualGoldRequest request) {
        return ResponseEntity.ok(virtualGoldService.buyVirtualGold(request));
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellVirtualGold(@Valid @RequestBody SellVirtualGoldRequest request) {
        return ResponseEntity.ok(virtualGoldService.sellVirtualGold(request));
    }
}