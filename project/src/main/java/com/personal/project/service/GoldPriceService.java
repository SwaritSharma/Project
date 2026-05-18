package com.personal.project.service;

import com.personal.project.dto.GoldPriceDTO;
import com.personal.project.dto.GoldPriceHistoryDTO;

import java.util.List;

public interface GoldPriceService {

    GoldPriceDTO getCurrentPrice();

    List<GoldPriceHistoryDTO> getPriceHistory(int days);

    GoldPriceDTO refreshCurrentPrice();
}
