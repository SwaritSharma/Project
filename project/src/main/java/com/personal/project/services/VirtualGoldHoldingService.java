package com.personal.project.services;

import com.personal.project.dtos.BuyVirtualGoldRequestDto;
import com.personal.project.dtos.SellVirtualGoldRequestDto;
import com.personal.project.dtos.VirtualGoldHoldingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VirtualGoldHoldingService {

    VirtualGoldHoldingResponseDto buyVirtualGold(
            Integer userId,
            BuyVirtualGoldRequestDto dto
    );

    VirtualGoldHoldingResponseDto sellVirtualGold(
            Integer userId,
            SellVirtualGoldRequestDto dto
    );

    Page<VirtualGoldHoldingResponseDto>
    getUserVirtualGoldHoldings(
            Integer userId,
            Pageable pageable
    );

    Page<VirtualGoldHoldingResponseDto>
    getUserVirtualGoldHoldingsSortedByQuantityDesc(
            Integer userId,
            Pageable pageable
    );

    Page<VirtualGoldHoldingResponseDto>
    getUserVirtualGoldHoldingsSortedByQuantityAsc(
            Integer userId,
            Pageable pageable
    );
}