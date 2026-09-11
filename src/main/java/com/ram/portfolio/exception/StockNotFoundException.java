package com.ram.portfolio.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(Long id) {
        super("Stock not found with id: " + id);
    }
}
