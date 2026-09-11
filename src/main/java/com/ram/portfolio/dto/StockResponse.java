package com.ram.portfolio.dto;

import com.ram.portfolio.entity.Stock;

import java.math.BigDecimal;

public record StockResponse(
        Long id,
        Long version,
        String stockName,
        BigDecimal buyPrice,
        Integer quantity,
        BigDecimal currentPrice
) {
    public static StockResponse from(Stock stock) {
        return new StockResponse(
                stock.getId(),
                stock.getVersion(),
                stock.getStockName(),
                stock.getBuyPrice(),
                stock.getQuantity(),
                stock.getCurrentPrice()
        );
    }
}
