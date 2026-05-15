package com.personal.project.controller;

import com.personal.project.dto.BuyPhysicalGoldRequest;
import com.personal.project.dto.ConvertToPhysicalGoldRequest;
import com.personal.project.entity.PhysicalGoldTransaction;
import com.personal.project.service.PhysicalGoldService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/physical-gold")
public class PhysicalGoldController {

    private final PhysicalGoldService
            physicalGoldService;

    public PhysicalGoldController(
            PhysicalGoldService physicalGoldService
    ) {

        this.physicalGoldService =
                physicalGoldService;
    }

    @PostMapping(
            "/buy"
    )
    public ResponseEntity<PhysicalGoldTransaction>
    buyPhysicalGold(
            @Valid
            @RequestBody
            BuyPhysicalGoldRequest request
    ) {

        PhysicalGoldTransaction transaction =
                physicalGoldService
                        .buyPhysicalGold(request);

        return ResponseEntity.ok(
                transaction
        );
    }

    @PostMapping(
            "/convert"
    )
    public ResponseEntity<PhysicalGoldTransaction>
    convertToPhysicalGold(
            @Valid
            @RequestBody
            ConvertToPhysicalGoldRequest request
    ) {

        PhysicalGoldTransaction transaction =
                physicalGoldService
                        .convertToPhysicalGold(request);

        return ResponseEntity.ok(
                transaction
        );
    }
}