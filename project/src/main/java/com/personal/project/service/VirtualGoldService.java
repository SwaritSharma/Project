package com.personal.project.service;

import com.personal.project.dto.BuyVirtualGoldRequest;
import com.personal.project.dto.HoldingDTO;
import com.personal.project.dto.SellVirtualGoldRequest;

public interface VirtualGoldService {

    HoldingDTO buyVirtualGold(
            BuyVirtualGoldRequest request
    );

    HoldingDTO sellVirtualGold(
            SellVirtualGoldRequest request
    );
}