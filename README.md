# Stock Portfolio Management API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build](https://github.com/Ram0897/stock-portfolio-api/actions/workflows/ci.yml/badge.svg)](https://github.com/Ram0897/stock-portfolio-api/actions/workflows/ci.yml)

A production-style REST API for managing stock portfolio holdings, built with **Java 17, Spring Boot, Spring Data JPA and PostgreSQL**.

The project focuses on backend engineering fundamentals: layered architecture, DTO boundaries, validation, precise monetary calculations, centralized exception handling, database migrations, containerization and automated testing.

## Features

- Create stock holdings with request validation
- Retrieve portfolio holdings through response DTOs
- Update market prices with proper `PUT` semantics
- Calculate invested value, current value and profit/loss
- Use `BigDecimal` for monetary calculations
- Centralized API error handling
- PostgreSQL persistence with Flyway migrations
- Docker Compose setup for API + PostgreSQL
- Unit tests for core portfolio behavior
- GitHub Actions CI

## Architecture

```text
Client
  |
  v
REST Controller
  |
  v
Request/Response DTOs
  |
  v
Service Layer
  |
  v
Spring Data JPA Repository
  |
  v
PostgreSQL
```

## Tech Stack

- **Java 17**
- **Spring Boot 4**
- **Spring Web MVC**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **Flyway**
- **Bean Validation**
- **Maven**
- **JUnit 5 / Mockito / AssertJ**
- **Docker / Docker Compose**
- **GitHub Actions**

## API

Base URL: `http://localhost:8080/api/stocks`

### Create a stock

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

```json
{
  "investedValue": 35000.00,
  "currentValue": 36000.00,
  "profit": 1000.00
}
```

### Update market price

`PUT /api/stocks/{id}/price`

```json
{
  "currentPrice": 3650.00
}
```

Invalid requests return a consistent JSON error payload instead of exposing internal exceptions.

## Run with Docker

Prerequisite: Docker Desktop or Docker Engine with Compose.

```bash
docker compose up --build
```

The API starts on `http://localhost:8080` and PostgreSQL is available on port `5432`.

Stop the stack with:

```bash
docker compose down
```

## Run locally without Docker

Set these environment variables for your local PostgreSQL instance:

```text
DB_URL=jdbc:postgresql://localhost:5432/portfolio
DB_USERNAME=portfolio
DB_PASSWORD=portfolio
```

Then run:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

## Tests

Run the test suite with:

```bash
./mvnw test
```

GitHub Actions runs the test suite automatically for pull requests and pushes to `main`.

## Project Structure

```text
src/main/java/com/ram/portfolio/
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
├── service/
└── StockPortfolioApplication.java

src/main/resources/
├── db/migration/
└── application.properties
```

## Engineering Roadmap

Next improvements planned for the portfolio:

- Integration tests with Testcontainers
- OpenAPI / Swagger documentation
- Pagination and filtering
- Authentication and authorization
- Database query optimization for portfolio aggregates
- Structured logging and observability
- Production deployment and infrastructure automation

## Why this project?

This repository is being evolved from a basic CRUD exercise into a portfolio-quality backend that demonstrates practical engineering decisions and trade-offs rather than only framework usage.

## License

This project is available for learning and portfolio purposes.
