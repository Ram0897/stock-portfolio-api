package com.ram.portfolio.repository;

import com.ram.portfolio.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Page<Stock> findByStockNameContainingIgnoreCase(String stockName, Pageable pageable);

    @Query("select coalesce(sum(s.currentPrice * s.quantity), 0) from Stock s")
    BigDecimal calculateCurrentPortfolioValue();

    @Query("select coalesce(sum(s.buyPrice * s.quantity), 0) from Stock s")
    BigDecimal calculateInvestedPortfolioValue();
}
