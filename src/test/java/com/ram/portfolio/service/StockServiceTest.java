package com.ram.portfolio.service;

import com.ram.portfolio.dto.CreateStockRequest;
import com.ram.portfolio.dto.PortfolioSummaryResponse;
import com.ram.portfolio.dto.StockResponse;
import com.ram.portfolio.dto.TradeStockRequest;
import com.ram.portfolio.dto.UpdateStockPriceRequest;
import com.ram.portfolio.entity.Stock;
import com.ram.portfolio.exception.StockConflictException;
import com.ram.portfolio.exception.StockNotFoundException;
import com.ram.portfolio.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository repository;

    private StockService service;

    @BeforeEach
    void setUp() {
        service = new StockService(repository);
    }

    @Test
    void shouldCalculatePortfolioSummary() {
        when(repository.calculateInvestedPortfolioValue()).thenReturn(new BigDecimal("1000.00"));
        when(repository.calculateCurrentPortfolioValue()).thenReturn(new BigDecimal("1250.00"));

        PortfolioSummaryResponse summary = service.getSummary();

        assertThat(summary.investedValue()).isEqualByComparingTo("1000.00");
        assertThat(summary.currentValue()).isEqualByComparingTo("1250.00");
        assertThat(summary.profit()).isEqualByComparingTo("250.00");
    }

    @Test
    void shouldAddStock() {
        Stock saved = new Stock("INFY", new BigDecimal("100.00"), 5, new BigDecimal("120.00"));
        when(repository.save(any(Stock.class))).thenReturn(saved);

        StockResponse response = service.addStock(
                new CreateStockRequest(" INFY ", new BigDecimal("100.00"), 5, new BigDecimal("120.00")));

        assertThat(response.stockName()).isEqualTo("INFY");
        assertThat(response.quantity()).isEqualTo(5);
    }

    @Test
    void shouldUpdateStockPriceWhenVersionMatches() {
        Stock stock = new Stock("TCS", new BigDecimal("300.00"), 2, new BigDecimal("310.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(stock));
        when(repository.saveAndFlush(stock)).thenReturn(stock);

        StockResponse response = service.updateStockPrice(1L,
                new UpdateStockPriceRequest(new BigDecimal("350.00"), 0L));

        assertThat(response.currentPrice()).isEqualByComparingTo("350.00");
    }

    @Test
    void shouldRejectStalePriceUpdate() {
        Stock stock = new Stock("TCS", new BigDecimal("300.00"), 2, new BigDecimal("310.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(stock));

        assertThatThrownBy(() -> service.updateStockPrice(1L,
                new UpdateStockPriceRequest(new BigDecimal("350.00"), 99L)))
                .isInstanceOf(StockConflictException.class);
    }

    @Test
    void shouldBuyAdditionalSharesTransactionally() {
        Stock stock = new Stock("TCS", new BigDecimal("300.00"), 2, new BigDecimal("310.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(stock));
        when(repository.save(stock)).thenReturn(stock);

        StockResponse response = service.buy(1L, new TradeStockRequest(3, new BigDecimal("320.00")));

        assertThat(response.quantity()).isEqualTo(5);
        assertThat(response.currentPrice()).isEqualByComparingTo("320.00");
    }

    @Test
    void shouldRejectSellingMoreSharesThanHeld() {
        Stock stock = new Stock("TCS", new BigDecimal("300.00"), 2, new BigDecimal("310.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(stock));

        assertThatThrownBy(() -> service.sell(1L, new TradeStockRequest(3, new BigDecimal("320.00"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("more shares");
    }

    @Test
    void shouldThrowWhenStockDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStockPrice(99L,
                new UpdateStockPriceRequest(new BigDecimal("350.00"), 0L)))
                .isInstanceOf(StockNotFoundException.class)
                .hasMessageContaining("99");
    }
}
