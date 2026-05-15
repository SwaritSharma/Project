package com.personal.project.service;

import com.personal.project.dto.BuyVirtualGoldRequest;
import com.personal.project.dto.SellVirtualGoldRequest;
import com.personal.project.entity.VirtualGoldHolding;

public interface VirtualGoldService {

    VirtualGoldHolding buyVirtualGold(
            BuyVirtualGoldRequest request
    );

    VirtualGoldHolding sellVirtualGold(
            SellVirtualGoldRequest request
    );
}