package com.personal.project.projection;

import java.math.BigDecimal;

public interface BranchSummaryProjection {

    String getVendorName();

    BigDecimal getQuantity();

    AddressProjection getAddress();
}