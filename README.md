# Stock Portfolio Management API

A Spring Boot REST API for managing a stock portfolio.  
Users can add stocks, view portfolio, update prices, and calculate portfolio value and profit.

## 🚀 Features
- Add a stock (Buy Stock)
- View all stocks in portfolio
- Update stock price
- Calculate total portfolio value
- Calculate invested value and profit
- In-memory / File-based H2 database

## 🛠 Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven

## 📦 API Endpoints

### 1. Add Stock
**POST** `/api/stocks`

```json
{
  "stockName": "TCS",
  "buyPrice": 3500,
  "currentPrice": 3600,
  "quantity": 10
}
