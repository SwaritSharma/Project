package com.personal.project.service;

import com.personal.project.dto.BuyPhysicalGoldRequest;
import com.personal.project.dto.ConvertToPhysicalGoldRequest;
import com.personal.project.dto.PhysicalGoldDTO;

public interface PhysicalGoldService {

    PhysicalGoldDTO buyPhysicalGold(
            BuyPhysicalGoldRequest request
    );

    PhysicalGoldDTO convertToPhysicalGold(
            ConvertToPhysicalGoldRequest request
    );
}