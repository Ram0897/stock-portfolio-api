package com.ram.portfolio.dto;

import java.math.BigDecimal;

public record PortfolioSummaryResponse(
        BigDecimal investedValue,
        BigDecimal currentValue,
        BigDecimal profit
) {
}
