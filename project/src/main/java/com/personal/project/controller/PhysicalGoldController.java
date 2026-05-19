package com.personal.project.controller;

import com.personal.project.dto.BuyPhysicalGoldRequest;
import com.personal.project.dto.ConvertToPhysicalGoldRequest;
import com.personal.project.dto.PhysicalGoldDTO;
import com.personal.project.service.PhysicalGoldService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/physical-gold")
@RequiredArgsConstructor
public class PhysicalGoldController {

    private final PhysicalGoldService physicalGoldService;

    @PostMapping("/buy")
    public ResponseEntity<PhysicalGoldDTO> buyPhysicalGold(@Valid @RequestBody BuyPhysicalGoldRequest request) {
        return ResponseEntity.ok(physicalGoldService.buyPhysicalGold(request));
    }

    @PostMapping("/convert")
    public ResponseEntity<PhysicalGoldDTO> convertToPhysicalGold(@Valid @RequestBody ConvertToPhysicalGoldRequest request) {
        return ResponseEntity.ok(physicalGoldService.convertToPhysicalGold(request));
    }
}
