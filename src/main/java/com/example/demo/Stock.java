package com.example.demo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity // Idi Database Table ani cheptundi
@Data   // Lombok: Getters, Setters, ToString automatic ga create chestundi
public class Stock {

    @Id // Idi Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment ID (1, 2, 3...)
    private Long id;

    private String stockName;    // Ex: REC Ltd
    private Double buyPrice;     // Ex: 383.00
    private Integer quantity;    // Ex: 100
    private Double currentPrice; // Ex: 385.00 (Profit calculate cheyadaniki)
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public Double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}
}