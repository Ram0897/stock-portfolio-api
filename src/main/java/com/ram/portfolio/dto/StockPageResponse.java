package com.ram.portfolio.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record StockPageResponse(
        List<StockResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static StockPageResponse from(Page<StockResponse> page) {
        return new StockPageResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
