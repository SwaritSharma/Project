package com.personal.project.controller;

import com.personal.project.dto.BuyVirtualGoldRequest;
import com.personal.project.dto.HoldingDTO;
import com.personal.project.dto.SellVirtualGoldRequest;
import com.personal.project.mapper.HoldingMapper;
import com.personal.project.service.GoldPriceService;
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
    private final HoldingMapper holdingMapper;
    private final GoldPriceService goldPriceService;

    @PostMapping("/buy")
    public ResponseEntity<HoldingDTO> buyVirtualGold(@Valid @RequestBody BuyVirtualGoldRequest request) {
        return ResponseEntity.ok(holdingMapper.toDto(
                virtualGoldService.buyVirtualGold(request),
                goldPriceService.getCurrentPrice().getPrice()
        ));
    }

    @PostMapping("/sell")
    public ResponseEntity<HoldingDTO> sellVirtualGold(@Valid @RequestBody SellVirtualGoldRequest request) {
        return ResponseEntity.ok(holdingMapper.toDto(
                virtualGoldService.sellVirtualGold(request),
                goldPriceService.getCurrentPrice().getPrice()
        ));
    }
}
