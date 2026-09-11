package com.ram.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record CreateStockRequest(
        @NotBlank String stockName,
        @NotNull @DecimalMin(value = "0.01") BigDecimal buyPrice,
        @NotNull @Positive Integer quantity,
        @NotNull @DecimalMin(value = "0.01") BigDecimal currentPrice
) {
}
