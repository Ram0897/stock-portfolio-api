package com.ram.portfolio.controller;

import com.ram.portfolio.dto.CreateStockRequest;
import com.ram.portfolio.dto.PortfolioSummaryResponse;
import com.ram.portfolio.dto.StockResponse;
import com.ram.portfolio.dto.UpdateStockPriceRequest;
import com.ram.portfolio.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StockResponse> addStock(@Valid @RequestBody CreateStockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addStock(request));
    }

    @GetMapping
    public List<StockResponse> getAllStocks() {
        return service.getAllStocks();
    }

    @GetMapping("/value")
    public BigDecimal getPortfolioValue() {
        return service.getTotalPortfolioValue();
    }

    @GetMapping("/summary")
    public PortfolioSummaryResponse getSummary() {
        return service.getSummary();
    }

    @PutMapping("/{id}/price")
    public StockResponse updateStockPrice(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStockPriceRequest request) {
        return service.updateStockPrice(id, request);
    }
}
