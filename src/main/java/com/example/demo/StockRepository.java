package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Ikkada em rayakkarledu!
    // JpaRepository manaki automatic ga save(), findAll(), delete() methods istundi.
}