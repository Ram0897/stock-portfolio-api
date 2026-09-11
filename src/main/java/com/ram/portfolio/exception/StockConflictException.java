package com.ram.portfolio.exception;

public class StockConflictException extends RuntimeException {

    public StockConflictException(Long id) {
        super("Stock " + id + " was modified by another request. Please retry with the latest version.");
    }
}
