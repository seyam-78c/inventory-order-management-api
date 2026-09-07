# Enterprise Inventory & Order Management API

A production-grade, containerized RESTful service engineered with **Java 21**, **Spring Boot 3**, and **PostgreSQL**. The platform manages stock inventories and processes orders with pessimistic row-level locking to guarantee zero overselling under concurrent load.

## Live Demo & Documentation
* **Interactive Swagger UI**: [https://inventory-order-management-api-ynlw.onrender.com/swagger-ui/index.html](https://inventory-order-management-api-ynlw.onrender.com/swagger-ui/index.html)
* **Base API URL**: `https://inventory-order-management-api-ynlw.onrender.com/api/v1`

---

## Architectural Highlights
* **Clean Layered Architecture**: Strict decoupling across Controller, Service, Repository, and DTO layers.
* **Concurrency Control**: Utilizes JPA pessimistic write locking (`PESSIMISTIC_WRITE`) during order placement to eliminate race conditions during inventory deductions.
* **Immutable Contracts**: Uses Java 21 `record` DTOs with Jakarta validation constraints (`@Valid`, `@NotBlank`, `@Min`, `@DecimalMin`).
* **Centralized Exception Handling**: Standardized RFC-compliant error envelopes via `@RestControllerAdvice`.
* **Serverless Cloud Database**: Integrated with Neon PostgreSQL connection pooling (`pgbouncer`).
* **Containerized Deployment**: Multi-stage `Dockerfile` (Maven build layer + lightweight Eclipse Temurin 21 JRE runtime) automated via Render.

---

## Core Endpoints

### Products (`/api/v1/products`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/` | Register a new product SKU with pricing and inventory |
| `GET` | `/` | Fetch all active catalog items |
| `GET` | `/{id}` | Retrieve item by identifier |
| `PUT` | `/{id}` | Update product specifications and inventory levels |
| `DELETE`| `/{id}` | Remove product from catalog |

### Orders (`/api/v1/orders`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/` | Place order (deducts stock atomically within transaction) |
| `GET` | `/` | Retrieve entire purchase order history |

---

## Local Development Setup

### Prerequisites
* Java 21 SDK
* Maven 3.9+
* Docker (optional)

### Build & Run
```bash
# Clone the repository
git clone [https://github.com/seyam-78c/inventory-order-management-api.git](https://github.com/seyam-78c/inventory-order-management-api.git)
cd inventory-order-management-api

# Compile and package
mvn clean package -DskipTests

# Run locally
mvn spring-boot:run
