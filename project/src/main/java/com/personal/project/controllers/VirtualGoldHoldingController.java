package com.personal.project.controllers;

import com.personal.project.dtos.BuyVirtualGoldRequestDto;
import com.personal.project.dtos.SellVirtualGoldRequestDto;
import com.personal.project.dtos.VirtualGoldHoldingResponseDto;
import com.personal.project.services.VirtualGoldHoldingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/virtual-gold")
@RequiredArgsConstructor
public class VirtualGoldHoldingController {

    private final VirtualGoldHoldingService
            virtualGoldHoldingService;

    @PostMapping("/{userId}/buy")
    public ResponseEntity<VirtualGoldHoldingResponseDto>
    buyVirtualGold(
            @PathVariable
            Integer userId,

            @Valid
            @RequestBody
            BuyVirtualGoldRequestDto dto
    ) {

        VirtualGoldHoldingResponseDto response =
                virtualGoldHoldingService
                        .buyVirtualGold(
                                userId,
                                dto
                        );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/{userId}/sell")
    public ResponseEntity<VirtualGoldHoldingResponseDto>
    sellVirtualGold(
            @PathVariable
            Integer userId,

            @Valid
            @RequestBody
            SellVirtualGoldRequestDto dto
    ) {

        VirtualGoldHoldingResponseDto response =
                virtualGoldHoldingService
                        .sellVirtualGold(
                                userId,
                                dto
                        );

        return ResponseEntity.ok(
                response
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<VirtualGoldHoldingResponseDto>>
    getUserVirtualGoldHoldings(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                virtualGoldHoldingService
                        .getUserVirtualGoldHoldings(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/sort/quantity-desc")
    public ResponseEntity<Page<VirtualGoldHoldingResponseDto>>
    getUserVirtualGoldHoldingsSortedByQuantityDesc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                virtualGoldHoldingService
                        .getUserVirtualGoldHoldingsSortedByQuantityDesc(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/sort/quantity-asc")
    public ResponseEntity<Page<VirtualGoldHoldingResponseDto>>
    getUserVirtualGoldHoldingsSortedByQuantityAsc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                virtualGoldHoldingService
                        .getUserVirtualGoldHoldingsSortedByQuantityAsc(
                                userId,
                                pageable
                        )
        );
    }
}