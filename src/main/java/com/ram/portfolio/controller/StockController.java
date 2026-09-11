package com.ram.portfolio.controller;

import com.ram.portfolio.dto.CreateStockRequest;
import com.ram.portfolio.dto.PortfolioSummaryResponse;
import com.ram.portfolio.dto.StockPageResponse;
import com.ram.portfolio.dto.StockResponse;
import com.ram.portfolio.dto.UpdateStockPriceRequest;
import com.ram.portfolio.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/stocks")
@Tag(name = "Stocks", description = "Portfolio holding management APIs")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Add a stock holding")
    public ResponseEntity<StockResponse> addStock(@Valid @RequestBody CreateStockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addStock(request));
    }

    @GetMapping
    @Operation(summary = "List portfolio holdings")
    public StockPageResponse getStocks(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        return service.getStocks(q, PageRequest.of(safePage, safeSize, Sort.by("stockName").ascending()));
    }

    @GetMapping("/value")
    @Operation(summary = "Get current portfolio value")
    public BigDecimal getPortfolioValue() {
        return service.getTotalPortfolioValue();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get portfolio performance summary")
    public PortfolioSummaryResponse getSummary() {
        return service.getSummary();
    }

    @PutMapping("/{id}/price")
    @Operation(summary = "Update the market price of a holding")
    public StockResponse updateStockPrice(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStockPriceRequest request) {
        return service.updateStockPrice(id, request);
    }
}
