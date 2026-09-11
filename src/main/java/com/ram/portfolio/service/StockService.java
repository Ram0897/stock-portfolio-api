package com.ram.portfolio.service;

import com.ram.portfolio.dto.CreateStockRequest;
import com.ram.portfolio.dto.PortfolioSummaryResponse;
import com.ram.portfolio.dto.StockPageResponse;
import com.ram.portfolio.dto.StockResponse;
import com.ram.portfolio.dto.UpdateStockPriceRequest;
import com.ram.portfolio.entity.Stock;
import com.ram.portfolio.exception.StockNotFoundException;
import com.ram.portfolio.repository.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class StockService {

    private final StockRepository repository;

    public StockService(StockRepository repository) {
        this.repository = repository;
    }

    public StockResponse addStock(CreateStockRequest request) {
        Stock stock = new Stock(request.stockName().trim(), request.buyPrice(), request.quantity(), request.currentPrice());
        return StockResponse.from(repository.save(stock));
    }

    @Transactional(readOnly = true)
    public StockPageResponse getStocks(String query, Pageable pageable) {
        Page<Stock> stocks = query == null || query.isBlank()
                ? repository.findAll(pageable)
                : repository.findByStockNameContainingIgnoreCase(query.trim(), pageable);
        return StockPageResponse.from(stocks.map(StockResponse::from));
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalPortfolioValue() {
        return repository.calculateCurrentPortfolioValue();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalInvestedValue() {
        return repository.calculateInvestedPortfolioValue();
    }

    @Transactional(readOnly = true)
    public PortfolioSummaryResponse getSummary() {
        BigDecimal invested = getTotalInvestedValue();
        BigDecimal current = getTotalPortfolioValue();
        return new PortfolioSummaryResponse(invested, current, current.subtract(invested));
    }

    public StockResponse updateStockPrice(Long id, UpdateStockPriceRequest request) {
        Stock stock = repository.findById(id).orElseThrow(() -> new StockNotFoundException(id));
        stock.setCurrentPrice(request.currentPrice());
        return StockResponse.from(repository.save(stock));
    }
}
