package com.ram.portfolio.service;

import com.ram.portfolio.dto.CreateStockRequest;
import com.ram.portfolio.dto.PortfolioSummaryResponse;
import com.ram.portfolio.dto.StockPageResponse;
import com.ram.portfolio.dto.StockResponse;
import com.ram.portfolio.dto.TradeStockRequest;
import com.ram.portfolio.dto.UpdateStockPriceRequest;
import com.ram.portfolio.entity.Stock;
import com.ram.portfolio.exception.StockConflictException;
import com.ram.portfolio.exception.StockNotFoundException;
import com.ram.portfolio.repository.StockRepository;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class StockService {

    private static final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository repository;

    public StockService(StockRepository repository) {
        this.repository = repository;
    }

    public StockResponse addStock(CreateStockRequest request) {
        Stock stock = new Stock(request.stockName().trim(), request.buyPrice(), request.quantity(), request.currentPrice());
        StockResponse response = StockResponse.from(repository.save(stock));
        log.info("Stock added: id={}, symbol={}, quantity={}", response.id(), response.stockName(), response.quantity());
        return response;
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
        verifyVersion(stock, request.version());
        stock.setCurrentPrice(request.currentPrice());
        StockResponse response = saveWithConflictHandling(stock, id);
        log.info("Stock price updated: id={}, version={}", response.id(), response.version());
        return response;
    }

    public StockResponse buy(Long id, TradeStockRequest request) {
        Stock stock = repository.findById(id).orElseThrow(() -> new StockNotFoundException(id));
        int previousQuantity = stock.getQuantity();
        stock.setQuantity(Math.addExact(previousQuantity, request.quantity()));
        stock.setCurrentPrice(request.price());
        StockResponse response = StockResponse.from(repository.save(stock));
        log.info("Stock purchase completed: id={}, quantityAdded={}, quantityTotal={}",
                id, request.quantity(), response.quantity());
        return response;
    }

    public StockResponse sell(Long id, TradeStockRequest request) {
        Stock stock = repository.findById(id).orElseThrow(() -> new StockNotFoundException(id));
        if (request.quantity() > stock.getQuantity()) {
            log.warn("Stock sale rejected: id={}, requestedQuantity={}, heldQuantity={}",
                    id, request.quantity(), stock.getQuantity());
            throw new IllegalArgumentException("Cannot sell more shares than currently held");
        }
        stock.setQuantity(stock.getQuantity() - request.quantity());
        stock.setCurrentPrice(request.price());
        StockResponse response = StockResponse.from(repository.save(stock));
        log.info("Stock sale completed: id={}, quantitySold={}, quantityRemaining={}",
                id, request.quantity(), response.quantity());
        return response;
    }

    private void verifyVersion(Stock stock, Long expectedVersion) {
        if (!stock.getVersion().equals(expectedVersion)) {
            log.warn("Stale stock update rejected: id={}, expectedVersion={}, actualVersion={}",
                    stock.getId(), expectedVersion, stock.getVersion());
            throw new StockConflictException(stock.getId());
        }
    }

    private StockResponse saveWithConflictHandling(Stock stock, Long id) {
        try {
            return StockResponse.from(repository.saveAndFlush(stock));
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            log.warn("Optimistic locking conflict: id={}", id);
            throw new StockConflictException(id);
        }
    }
}
