package com.ram.portfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private String stockName;
    private BigDecimal buyPrice;
    private Integer quantity;
    private BigDecimal currentPrice;

    protected Stock() {
    }

    public Stock(String stockName, BigDecimal buyPrice, Integer quantity, BigDecimal currentPrice) {
        this.stockName = stockName;
        this.buyPrice = buyPrice;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
    }

    public Long getId() { return id; }
    public Long getVersion() { return version; }
    public String getStockName() { return stockName; }
    public BigDecimal getBuyPrice() { return buyPrice; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getCurrentPrice() { return currentPrice; }

    public void setStockName(String stockName) { this.stockName = stockName; }
    public void setBuyPrice(BigDecimal buyPrice) { this.buyPrice = buyPrice; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
}
