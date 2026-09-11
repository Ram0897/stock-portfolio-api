# Stock Portfolio Management API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![CI](https://github.com/Ram0897/stock-portfolio-api/actions/workflows/ci.yml/badge.svg)](https://github.com/Ram0897/stock-portfolio-api/actions/workflows/ci.yml)

A production-oriented REST API for managing stock portfolio holdings. Built to demonstrate backend engineering practices beyond basic CRUD: layered architecture, validation, consistent errors, PostgreSQL, Flyway, pagination, database-side aggregation, OpenAPI documentation, Docker and automated tests.

## Architecture

```text
Client
  |
  v
REST Controller + Validation + OpenAPI
  |
  v
Service Layer
  |
  v
Spring Data JPA Repository
  |
  v
PostgreSQL <--- Flyway migrations
```

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway
- Bean Validation
- OpenAPI / Swagger UI
- JUnit + Mockito
- Testcontainers
- Maven
- Docker / Docker Compose
- GitHub Actions

## API

Base URL: `http://localhost:8080/api/stocks`

### Create holding

`POST /api/stocks`

```json
{
  "stockName": "TCS",
  "buyPrice": 3500.00,
  "currentPrice": 3600.00,
  "quantity": 10
}
```

### List holdings

`GET /api/stocks?page=0&size=20`

Optional search:

`GET /api/stocks?q=TCS&page=0&size=20`

The API limits page size to 100 and sorts holdings by stock name.

### Portfolio value

`GET /api/stocks/value`

### Portfolio summary

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

## API Documentation

When running locally, Swagger UI is available at:

`http://localhost:8080/swagger-ui.html`

OpenAPI JSON:

`http://localhost:8080/v3/api-docs`

## Run with Docker

```bash
docker compose up --build
```

The API will be available on port `8080` and PostgreSQL on port `5432`.

## Run locally

Prerequisites:

- JDK 17+
- Docker Desktop (recommended for integration tests)

Run the application:

```bash
./mvnw spring-boot:run
```

Run the full test suite:

```bash
./mvnw test
```

The integration test uses Testcontainers to start an isolated PostgreSQL instance, so Docker must be available when running it.

## Configuration

Database configuration is environment-variable driven:

```text
DB_URL=jdbc:postgresql://localhost:5432/portfolio
DB_USERNAME=portfolio
DB_PASSWORD=portfolio
```

Database schema changes are managed by Flyway under `src/main/resources/db/migration`.

## Engineering Highlights

- **Money-safe calculations:** `BigDecimal` instead of floating-point `Double` for monetary values.
- **Separation of concerns:** controllers handle HTTP, services handle business logic, repositories handle persistence.
- **API contracts:** JPA entities are not exposed directly; request/response DTOs define the API surface.
- **Validation:** invalid prices, quantities and blank stock names are rejected at the API boundary.
- **Consistent errors:** global exception handling returns structured API errors.
- **Scalability basics:** holdings support pagination/search and portfolio totals use database aggregation instead of loading every row into application memory.
- **Database reliability:** PostgreSQL plus versioned Flyway migrations.
- **Test strategy:** unit tests for business logic plus a PostgreSQL integration test using Testcontainers.
- **Delivery:** Docker packaging and GitHub Actions CI.

## Project Structure

```text
src/main/java/com/ram/portfolio
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
└── StockPortfolioApplication.java
```

## Roadmap

- Authentication and authorization
- Optimistic locking for concurrent price updates
- Transactional portfolio/order workflows
- Redis caching where profiling justifies it
- Structured logging and observability
- Contract/integration testing for external market-data providers

## License

For learning and portfolio purposes.
