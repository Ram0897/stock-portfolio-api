package com.ram.portfolio.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record UpdateStockPriceRequest(
        @NotNull @DecimalMin(value = "0.01") BigDecimal currentPrice
) {
}
