package com.personal.project.controller;

import com.personal.project.dto.BuyVirtualGoldRequest;
import com.personal.project.dto.SellVirtualGoldRequest;
import com.personal.project.entity.VirtualGoldHolding;
import com.personal.project.service.VirtualGoldService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/virtual-gold")
public class VirtualGoldController {

    private final VirtualGoldService
            virtualGoldService;

    public VirtualGoldController(
            VirtualGoldService virtualGoldService
    ) {

        this.virtualGoldService =
                virtualGoldService;
    }

    @PostMapping(
            "/buy"
    )
    public ResponseEntity<VirtualGoldHolding>
    buyVirtualGold(
            @Valid
            @RequestBody
            BuyVirtualGoldRequest request
    ) {

        VirtualGoldHolding holding =
                virtualGoldService
                        .buyVirtualGold(request);

        return ResponseEntity.ok(
                holding
        );
    }

    @PostMapping(
            "/sell"
    )
    public ResponseEntity<VirtualGoldHolding>
    sellVirtualGold(
            @Valid
            @RequestBody
            SellVirtualGoldRequest request
    ) {

        VirtualGoldHolding holding =
                virtualGoldService
                        .sellVirtualGold(request);

        return ResponseEntity.ok(
                holding
        );
    }
}