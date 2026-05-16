package com.personal.project.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class GoldPriceHistoryDTO {
    private String date;
    private BigDecimal price;
}
