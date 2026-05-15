package com.personal.project.services;

import com.personal.project.dtos.BuyPhysicalGoldRequestDto;
import com.personal.project.dtos.ConvertVirtualToPhysicalRequestDto;
import com.personal.project.dtos.ConvertVirtualToPhysicalResponseDto;
import com.personal.project.dtos.PhysicalGoldTransactionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhysicalGoldTransactionService {

    ConvertVirtualToPhysicalResponseDto
    convertVirtualToPhysical(
            Integer userId,
            ConvertVirtualToPhysicalRequestDto dto
    );

    PhysicalGoldTransactionResponseDto
    buyPhysicalGold(
            Integer userId,
            BuyPhysicalGoldRequestDto dto
    );

    Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactions(
            Integer userId,
            Pageable pageable
    );

    Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsByBranch(
            Integer userId,
            Integer branchId,
            Pageable pageable
    );

    Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsSortedByCreatedAtDesc(
            Integer userId,
            Pageable pageable
    );

    Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsSortedByCreatedAtAsc(
            Integer userId,
            Pageable pageable
    );

    Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsSortedByQuantityDesc(
            Integer userId,
            Pageable pageable
    );

    Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsSortedByQuantityAsc(
            Integer userId,
            Pageable pageable
    );
}