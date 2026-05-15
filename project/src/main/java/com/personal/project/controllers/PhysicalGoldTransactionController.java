package com.personal.project.controllers;

import com.personal.project.dtos.BuyPhysicalGoldRequestDto;
import com.personal.project.dtos.ConvertVirtualToPhysicalRequestDto;
import com.personal.project.dtos.ConvertVirtualToPhysicalResponseDto;
import com.personal.project.dtos.PhysicalGoldTransactionResponseDto;
import com.personal.project.services.PhysicalGoldTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/physical-gold")
@RequiredArgsConstructor
public class PhysicalGoldTransactionController {

    private final PhysicalGoldTransactionService
            physicalGoldTransactionService;

    @PostMapping("/{userId}/convert")
    public ResponseEntity<ConvertVirtualToPhysicalResponseDto>
    convertVirtualToPhysical(
            @PathVariable
            Integer userId,

            @Valid
            @RequestBody
            ConvertVirtualToPhysicalRequestDto dto
    ) {

        ConvertVirtualToPhysicalResponseDto response =
                physicalGoldTransactionService
                        .convertVirtualToPhysical(
                                userId,
                                dto
                        );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/{userId}/buy")
    public ResponseEntity<PhysicalGoldTransactionResponseDto>
    buyPhysicalGold(
            @PathVariable
            Integer userId,

            @Valid
            @RequestBody
            BuyPhysicalGoldRequestDto dto
    ) {

        PhysicalGoldTransactionResponseDto response =
                physicalGoldTransactionService
                        .buyPhysicalGold(
                                userId,
                                dto
                        );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<PhysicalGoldTransactionResponseDto>>
    getUserPhysicalGoldTransactions(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                physicalGoldTransactionService
                        .getUserPhysicalGoldTransactions(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/branch/{branchId}")
    public ResponseEntity<Page<PhysicalGoldTransactionResponseDto>>
    getUserPhysicalGoldTransactionsByBranch(
            @PathVariable
            Integer userId,

            @PathVariable
            Integer branchId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                physicalGoldTransactionService
                        .getUserPhysicalGoldTransactionsByBranch(
                                userId,
                                branchId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/sort/created-at-desc")
    public ResponseEntity<Page<PhysicalGoldTransactionResponseDto>>
    getUserPhysicalGoldTransactionsSortedByCreatedAtDesc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                physicalGoldTransactionService
                        .getUserPhysicalGoldTransactionsSortedByCreatedAtDesc(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/sort/created-at-asc")
    public ResponseEntity<Page<PhysicalGoldTransactionResponseDto>>
    getUserPhysicalGoldTransactionsSortedByCreatedAtAsc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                physicalGoldTransactionService
                        .getUserPhysicalGoldTransactionsSortedByCreatedAtAsc(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/sort/quantity-desc")
    public ResponseEntity<Page<PhysicalGoldTransactionResponseDto>>
    getUserPhysicalGoldTransactionsSortedByQuantityDesc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                physicalGoldTransactionService
                        .getUserPhysicalGoldTransactionsSortedByQuantityDesc(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/sort/quantity-asc")
    public ResponseEntity<Page<PhysicalGoldTransactionResponseDto>>
    getUserPhysicalGoldTransactionsSortedByQuantityAsc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                physicalGoldTransactionService
                        .getUserPhysicalGoldTransactionsSortedByQuantityAsc(
                                userId,
                                pageable
                        )
        );
    }
}