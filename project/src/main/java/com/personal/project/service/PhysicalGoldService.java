package com.personal.project.service;

import com.personal.project.dto.BuyPhysicalGoldRequest;
import com.personal.project.dto.ConvertToPhysicalGoldRequest;
import com.personal.project.entity.PhysicalGoldTransaction;

public interface PhysicalGoldService {

    PhysicalGoldTransaction buyPhysicalGold(
            BuyPhysicalGoldRequest request
    );

    PhysicalGoldTransaction
    convertToPhysicalGold(
            ConvertToPhysicalGoldRequest request
    );
}