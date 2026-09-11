# Stock Portfolio Management API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![CI](https://github.com/Ram0897/stock-portfolio-api/actions/workflows/ci.yml/badge.svg)](https://github.com/Ram0897/stock-portfolio-api/actions/workflows/ci.yml)

A production-oriented REST API for managing stock portfolio holdings. Built to demonstrate backend engineering practices beyond basic CRUD: layered architecture, JWT authentication, validation, consistent errors, optimistic locking, PostgreSQL, Flyway, pagination, database-side aggregation, transactional buy/sell workflows, structured logging, OpenAPI documentation, Docker and automated tests.

## Architecture

```text
Client
  |
  v
JWT Authentication Filter
  |
  v
REST Controller + Validation + OpenAPI
  |
  v
Service Layer + Transaction Boundaries + Logging
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
- Spring Security
- JWT (JJWT)
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

## Authentication

The portfolio endpoints require a valid JWT bearer token. Login is intentionally kept simple for this portfolio project: credentials are supplied through environment variables and the authenticated user is held in memory.

### Login

`POST /api/auth/login`

```json
{
  "username": "portfolio-user",
  "password": "change-me"
}
```

Response:

```json
{
  "token": "<jwt>",
  "tokenType": "Bearer",
  "expiresInSeconds": 3600
}
```

Send the token on protected requests:

```text
Authorization: Bearer <jwt>
```

For real deployments, replace the demo in-memory identity store with persistent users, refresh-token rotation and a dedicated identity provider or hardened credential-management flow.

## Concurrency Control

Stock price updates use JPA optimistic locking with a persisted `version` field. Clients receive the current version with each stock response and must send that version when updating the price.

Example update:

```json
{
  "currentPrice": 3650.00,
  "version": 0
}
```

If another request has already modified the same stock, the API returns **409 Conflict** instead of silently overwriting the newer value.

## Trading Workflows

Holdings support transactional buy and sell operations:

- `POST /api/stocks/{id}/buy` increases the holding quantity and updates the market price.
- `POST /api/stocks/{id}/sell` decreases the holding quantity and rejects sales above the currently held quantity.
- Trade operations execute inside the service transaction boundary.
- Business events are logged with SLF4J without logging credentials or JWT secrets.

Example trade request:

```json
{
  "quantity": 5,
  "price": 3650.00
}
```

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
  "currentPrice": 3650.00,
  "version": 0
}
```

### Buy shares

`POST /api/stocks/{id}/buy`

### Sell shares

`POST /api/stocks/{id}/sell`

## API Documentation

When running locally, Swagger UI is available at:

`http://localhost:8080/swagger-ui.html`

OpenAPI JSON:

`http://localhost:8080/v3/api-docs`

Authentication is required for stock endpoints; `/api/auth/login` and the OpenAPI documentation endpoints remain public.

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

Authentication configuration:

```text
AUTH_USERNAME=portfolio-user
AUTH_PASSWORD=change-me
JWT_SECRET=change-this-development-secret-key-32-bytes-min
JWT_EXPIRATION_SECONDS=3600
```

Use strong, unique secrets in any shared or production environment. Database schema changes are managed by Flyway under `src/main/resources/db/migration`.

## Engineering Highlights

- **Authentication:** stateless JWT bearer authentication with BCrypt-protected in-memory credentials.
- **Concurrency safety:** optimistic locking prevents lost updates during concurrent price changes and maps conflicts to HTTP 409.
- **Money-safe calculations:** `BigDecimal` instead of floating-point `Double` for monetary values.
- **Separation of concerns:** controllers handle HTTP, services handle business logic, repositories handle persistence.
- **API contracts:** JPA entities are not exposed directly; request/response DTOs define the API surface.
- **Validation:** invalid prices, quantities and blank stock names are rejected at the API boundary.
- **Consistent errors:** global exception handling returns structured API errors without leaking internal exception details.
- **Transactional workflows:** buy/sell operations enforce business rules inside service-level transactions.
- **Observability basics:** structured SLF4J logs capture important business events and concurrency conflicts without sensitive values.
- **Scalability basics:** holdings support pagination/search and portfolio totals use database aggregation instead of loading every row into application memory.
- **Database reliability:** PostgreSQL plus versioned Flyway migrations.
- **Test strategy:** unit tests for business logic plus a PostgreSQL integration test using Testcontainers.
- **Delivery:** Docker packaging and GitHub Actions CI.

## Project Structure

```text
src/main/java/com/ram/portfolio
├── auth
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
└── StockPortfolioApplication.java
```

## Roadmap

- Persistent users and refresh-token rotation
- Role-based authorization
- Trade/order history with a dedicated transaction model
- Redis caching where profiling justifies it
- Correlation IDs and centralized observability
- Contract/integration testing for external market-data providers

## License

For learning and portfolio purposes.
