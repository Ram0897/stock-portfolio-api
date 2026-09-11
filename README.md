# Stock Portfolio Management API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build](https://github.com/Ram0897/stock-portfolio-api/actions/workflows/ci.yml/badge.svg)](https://github.com/Ram0897/stock-portfolio-api/actions/workflows/ci.yml)

A backend REST API for managing an investment portfolio, built with **Java 17, Spring Boot, Spring Data JPA and H2**.

The project demonstrates layered backend design, RESTful API development, persistence, portfolio calculations, validation opportunities and automated CI.

## What it does

- Add stocks to a portfolio
- Retrieve portfolio holdings
- Update the current market price of a holding
- Calculate total invested value
- Calculate current portfolio value
- Calculate total profit/loss
- Persist portfolio data using H2

## Architecture

```text
Client
  |
  v
REST Controller
  |
  v
Service Layer
  |
  v
Spring Data JPA Repository
  |
  v
H2 Database
```

## Tech Stack

- **Java 17**
- **Spring Boot 4**
- **Spring Web MVC**
- **Spring Data JPA / Hibernate**
- **H2 Database**
- **Maven**
- **JUnit / Spring Boot Test**
- **GitHub Actions**

## API

Base URL: `http://localhost:8080/api/stocks`

### Add a stock

`POST /api/stocks`

```json
{
  "stockName": "TCS",
  "buyPrice": 3500.00,
  "currentPrice": 3600.00,
  "quantity": 10
}
```

### Get all holdings

`GET /api/stocks`

### Get current portfolio value

`GET /api/stocks/value`

### Get portfolio summary

`GET /api/stocks/summary`

Example response:

```json
{
  "investedValue": 35000.0,
  "currentValue": 36000.0,
  "profit": 1000.0
}
```

### Update market price

`PUT /api/stocks/{id}/price?value=3650.00`

## Run locally

### Prerequisites

- JDK 17+
- Maven Wrapper (included in the repository)

### Start the application

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

### Run tests

```bash
./mvnw test
```

## Database

The default local configuration uses a file-backed H2 database at `./data/portfolio-db` so portfolio data can survive application restarts.

The H2 console is available locally at:

`http://localhost:8080/h2-console`

> The H2 console is intended for local development only and should not be exposed in a production deployment.

## Engineering roadmap

This repository is intentionally being evolved toward production-style backend engineering. Planned improvements include:

- PostgreSQL support
- Request/response DTOs and Bean Validation
- Global exception handling with consistent API errors
- Unit and integration test coverage
- Testcontainers
- Database migrations with Flyway
- Docker and Docker Compose
- OpenAPI / Swagger documentation
- Authentication and authorization
- Redis caching
- Observability and structured logging

## Project structure

```text
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── Stock.java
│   │   ├── StockController.java
│   │   ├── StockRepository.java
│   │   ├── StockService.java
│   │   └── StockPortfolioApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/demo/
```

## Why this project?

The project is a practical backend exercise focused on the core engineering concepts used in production APIs: separation of concerns, persistence, business logic, HTTP semantics, testing and continuous integration.

## License

This project is available for learning and portfolio purposes.
