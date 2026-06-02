# RouteOptima - Logistics Backend Engine

RouteOptima is a robust, production-grade backend engine designed for calculating and optimizing logistics routing. Built with **Spring Boot 4.0.3**, it simulates a high-traffic enterprise environment by integrating external mapping and environmental APIs (LocationIQ and OpenWeather). The architecture handles heavy computational loads through asynchronous message queues, high-speed in-memory caching, and robust security protocols.

---

## Key Features and Architecture

**Secure JWT Authentication**
Stateless security filter chain utilizing JSON Web Tokens to lock down endpoints and protect internal routing data.

**Database Versioning via Flyway**
Automated, idempotent schema migrations guaranteeing consistent PostgreSQL database state across all environments.

**High-Performance Caching with Redis**
Intercepts repetitive external API calls (LocationIQ geocoding and OpenWeather data) using Spring Cache, reducing response times from approximately 500ms down to under 10ms.

**Weather-Aware Dynamic Routing**
Integrates the OpenWeather API to pull live weather conditions along shipping routes, enabling the engine to calculate dynamic delivery delays or weather-based pricing premiums.

**API Rate Limiting via Bucket4j**
Protects compute-heavy endpoints from abuse using the token-bucket algorithm, returning `429 Too Many Requests` when limits are breached.

**Asynchronous Task Processing with RabbitMQ**
Decouples heavy route-optimization calculations from the main HTTP thread by passing payloads to background worker queues.

**Automated System Maintenance**
Utilizes Spring `@Scheduled` cron jobs to manage background tasks and system health monitoring.

**Data Pagination**
Implements Spring Data JPA `Pageable` to efficiently slice large dataset queries, such as retrieving thousands of shipment records.

**Automated Testing Suite**
- Unit testing of core business logic via **JUnit 5 and Mockito**
- Full integration testing using **Testcontainers** to spin up isolated Docker instances of PostgreSQL, Redis, and RabbitMQ during the build phase

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 17 |
| Framework | Spring Boot | 4.0.3 |
| Database | PostgreSQL | 42.6.0 |
| Caching | Redis (Spring Data Redis) | managed by Spring Boot |
| Message Broker | RabbitMQ (Spring AMQP) | managed by Spring Boot |
| Security | Spring Security and JJWT | 0.12.3 |
| Migrations | Flyway (PostgreSQL dialect) | managed by Spring Boot |
| Rate Limiting | Bucket4j | 8.7.0 |
| API Documentation | SpringDoc OpenAPI (Swagger UI) | 2.8.8 |
| Testing | JUnit 5, Mockito, Testcontainers (PostgreSQL) | 1.20.6 |
| Build | Maven Surefire Plugin | 3.5.4 |

---

## Prerequisites

To run this engine locally, the following must be installed on your machine:

- **Java Development Kit (JDK) 17** (as specified in `pom.xml`)
- **Maven 3.8+**
- **Docker Desktop** (required for local backing services and Testcontainers)

---

## Local Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/routeOptima.git
cd routeOptima/logistics-engine
```

### 2. Spin Up the Backing Infrastructure via Docker

PostgreSQL, Redis, and RabbitMQ instances must be running locally before starting the application. Boot these instantly using the following commands:

```bash
# PostgreSQL Database
docker run -d --name route-optima-db -p 5433:5432 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  postgres:15-alpine

# Redis Cache
docker run -d --name route-optima-cache -p 6379:6379 redis:7-alpine

# RabbitMQ Message Broker
docker run -d --name route-optima-queue \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

### 3. Configure Environment Variables

Ensure `src/main/resources/application-local.yml` contains your active API keys:

```yaml
jwt:
  secret: "YOUR_GENERATED_SECURE_SECRET_STRING_HERE"
locationiq:
  api-key: "YOUR_LOCATION_IQ_API_KEY"
openweather:
  api-key: "YOUR_OPEN_WEATHER_API_KEY"
```

### 4. Run the Application

Start the Spring Boot server. Flyway will automatically detect the empty database and build the schema on startup.

```bash
mvn clean spring-boot:run
```

The API will be live at `http://localhost:8080`.

---

## Running the Test Suite

This project uses a dual-layer testing strategy combining unit and integration tests. Integration tests leverage Testcontainers to automatically spin up temporary Docker containers for the database and message broker, ensuring consistent test execution regardless of the local environment.

To run the complete test suite:

```bash
mvn clean test -Duser.timezone="Asia/Kolkata"
```

> **Note:** The explicit timezone argument ensures the PostgreSQL test container safely synchronizes with local system environments.

---

## Core Directory Structure

```
src/main/java/com/routeoptima/logistics_engine/
├── config/           # Redis, RabbitMQ, and Security Beans
├── controller/       # REST API Endpoints (Auth, Shipments, Weather)
├── model/            # JPA Entities and DTOs
├── queue/            # RabbitMQ Producer and Consumer logic
├── repository/       # Spring Data JPA Interfaces
├── security/         # JWT Filters and Bucket4j Rate Limiting
└── service/          # Core business logic, pricing math, and external API integrations
```

---

## External API Integrations

**LocationIQ**
Handles geocoding, reverse geocoding, and physical coordinate distance matrix generation.

**OpenWeather**
Pulls real-time localized weather data to evaluate environmental constraints on live routes.

All third-party API responses are cached in Redis to minimize network latency, reduce outbound data costs, and adhere to upstream rate limit boundaries.