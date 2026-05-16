package com.personal.project.controller;

import com.personal.project.dto.GoldPriceDTO;
import com.personal.project.dto.GoldPriceHistoryDTO;
import com.personal.project.service.GoldPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gold")
@RequiredArgsConstructor
public class GoldPriceController {

    private final GoldPriceService goldPriceService;

    @GetMapping("/price")
    public GoldPriceDTO getCurrentPrice() {
        return goldPriceService.getCurrentPrice();
    }

    @GetMapping("/price-history")
    public List<GoldPriceHistoryDTO> getPriceHistory(@RequestParam(defaultValue = "30") int days) {
        return goldPriceService.getPriceHistory(days);
    }
}
