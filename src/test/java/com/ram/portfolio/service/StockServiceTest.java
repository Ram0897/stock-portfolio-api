package com.ram.portfolio.service;

import com.ram.portfolio.dto.CreateStockRequest;
import com.ram.portfolio.dto.PortfolioSummaryResponse;
import com.ram.portfolio.dto.StockResponse;
import com.ram.portfolio.dto.UpdateStockPriceRequest;
import com.ram.portfolio.entity.Stock;
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
        Stock stock = new Stock("RELIANCE", new BigDecimal("100.00"), 10, new BigDecimal("125.00"));
        when(repository.findAll()).thenReturn(List.of(stock));

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
    void shouldUpdateStockPrice() {
        Stock stock = new Stock("TCS", new BigDecimal("300.00"), 2, new BigDecimal("310.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(stock));
        when(repository.save(stock)).thenReturn(stock);

        StockResponse response = service.updateStockPrice(1L,
                new UpdateStockPriceRequest(new BigDecimal("350.00")));

        assertThat(response.currentPrice()).isEqualByComparingTo("350.00");
    }

    @Test
    void shouldThrowWhenStockDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStockPrice(99L,
                new UpdateStockPriceRequest(new BigDecimal("350.00"))))
                .isInstanceOf(StockNotFoundException.class)
                .hasMessageContaining("99");
    }
}
