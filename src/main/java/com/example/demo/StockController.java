package com.example.demo;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController // Idi API create chestundi (JSON responses istundi)
@RequestMapping("/api/stocks") // Common URL prefix (ex: localhost:8080/api/stocks)
public class StockController {

    @Autowired
    private StockService service;

    // 1. Stock Add Cheyadaniki (POST Request)
    // URL: http://localhost:8080/api/stocks
    @PostMapping
    public Stock addStock(@RequestBody Stock stock) {
        return service.addStock(stock);
    }

    // 2. Anni Stocks Chudadaniki (GET Request)
    // URL: http://localhost:8080/api/stocks
    @GetMapping
    public List<Stock> getAllStocks() {
        return service.getAllStocks();
    }

    // 3. Portfolio Value Chudadaniki (GET Request)
    // URL: http://localhost:8080/api/stocks/value
    @GetMapping("/value")
    public Double getPortfolioValue() {
        return service.getTotalPortfolioValue();
    }
    @GetMapping("/summary")
    public Map<String, Double> getSummary() {
        Map<String, Double> map = new HashMap<>();
        map.put("investedValue", service.getTotalInvestedValue());
        map.put("currentValue", service.getTotalPortfolioValue());
        map.put("profit", service.getTotalProfit());
        return map;
    }
 // 5. Update Stock Price (PUT Request)
 // URL: http://localhost:8080/api/stocks/{id}/price?value=90.25
  @GetMapping("/{id}/price")
 public Stock updateStockPrice(
         @PathVariable Long id,
         @RequestParam("value") Double newPrice) {

     return service.updateStockPrice(id, newPrice);
 }

}