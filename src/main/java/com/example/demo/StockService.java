package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Spring ki cheptunnam: "Idi Service Layer, Business Logic ikkada untadi"
public class StockService {

    @Autowired // Repository ni automatic ga connect chestundi (Dependency Injection)
    private StockRepository repository;

    // 1. Stock Konnappudu Save cheyadaniki (Buy Stock)
    public Stock addStock(Stock stock) {
        return repository.save(stock);
    }

    // 2. Portfolio lo unna Stocks anni chudadaniki (View Portfolio)
    public List<Stock> getAllStocks() {
        return repository.findAll();
    }
    

    // 3. Total Portfolio Value Calculate cheyadaniki
    // (Example: 100 shares * 385 price = 38500 Value)
    public Double getTotalPortfolioValue() {
        List<Stock> stocks = repository.findAll();
        double totalValue = 0;
        for (Stock s : stocks) {
            // Null check important real coding lo, but practice ki direct ga rastunna
            if (s.getCurrentPrice() != null && s.getQuantity() != null) {
                totalValue += (s.getCurrentPrice() * s.getQuantity());
            }
        }
        return totalValue;
    }
    public Double getTotalInvestedValue() {
        List<Stock> stocks = repository.findAll();
        double invested = 0;

        for (Stock s : stocks) {
            if (s.getBuyPrice() != null && s.getQuantity() != null) {
                invested += s.getBuyPrice() * s.getQuantity();
            }
        }
        return invested;
    }

    // 5. Total Profit = Current Value - Invested Value
    public Double getTotalProfit() {
        return getTotalPortfolioValue() - getTotalInvestedValue();
    }
    public Stock updateStockPrice(Long id, Double newPrice) {
        Stock stock = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found with id: " + id));

        stock.setCurrentPrice(newPrice);
        return repository.save(stock);
    }
}