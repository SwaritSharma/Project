package com.personal.project.projection;

import java.math.BigDecimal;

public interface HoldingSummaryProjection {

    String getVendorName();

    BigDecimal getQuantity();

    BigDecimal getCurrentGoldPrice();

    AddressProjection getBranchAddress();
}