# Ecommerce API

A RESTful ecommerce backend built with Spring Boot 4, Java 21, and MySQL. Supports product catalogue management, order lifecycle, and product reviews — with soft deletes, JPA auditing, Flyway migrations, and New Relic monitoring.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [Environment Variables](#environment-variables)
- [Running the Application](#running-the-application)
- [Database Migrations](#database-migrations)
- [API Reference](#api-reference)
  - [Categories](#categories)
  - [Products](#products)
  - [Orders](#orders)
  - [Reviews](#reviews)
- [Data Models](#data-models)
- [Response Format](#response-format)
- [Error Handling](#error-handling)
- [Key Features](#key-features)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8 |
| Migrations | Flyway |
| Build | Gradle |
| Monitoring | New Relic |
| Config | dotenv-java 3.2.0 |

---

## Project Structure

```
src/main/java/com/brainstormer/ecommerce/
├── adapters/           # Entity-to-DTO conversion components
├── controllers/        # REST controllers (request routing)
├── dtos/               # Request and response DTOs
├── exceptions/         # Custom exceptions and global handler
├── repositories/       # JPA repositories (data access)
├── schema/             # JPA entity classes
│   └── enums/          # Enum types (OrderStatus, OrderItemAction)
├── services/           # Business logic layer
└── utlis/              # Shared utilities (ApiResponse wrapper)

src/main/resources/
├── application.yml     # App configuration
└── db/migrations/      # Flyway SQL migration scripts
```

---

## Setup & Installation

### Prerequisites

- Java 21+
- MySQL 8
- Gradle (or use the included `./gradlew` wrapper)

### Steps

1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd ecommerce
   ```

2. **Create the MySQL database**
   ```sql
   CREATE DATABASE ecommercedb;
   ```

3. **Create a `.env` file** in the project root (see [Environment Variables](#environment-variables))

4. **Run migrations and start the server** (see [Running the Application](#running-the-application))

---

## Environment Variables

Create a `.env` file in the project root with the following variables:

```env
USER_NAME=your_mysql_username
PASSWORD=your_mysql_password
```

These are loaded automatically at startup via dotenv-java and injected into `application.yml`.

---

## Running the Application

```bash
./gradlew bootRun
```

The application starts on `http://localhost:8080` and prints:

```
Ecommerce application is live......
```

> **Note:** The `bootRun` task is pre-configured to attach the New Relic Java agent (`newrelic/newrelic.jar`). Ensure `newrelic.yml` is present if monitoring is required.

---

## Database Migrations

Flyway manages schema versioning. Migration scripts live in `src/main/resources/db/migrations/` and run automatically on startup.

- DDL mode is set to `validate` — Hibernate will not auto-create/alter tables.
- To add a migration, create a new file following Flyway naming: `V<version>__description.sql`.

---

## API Reference

All endpoints are prefixed with `/api/v1`. All responses use the [standard response wrapper](#response-format).

---

### Categories

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/categories` | Create a new category |
| `GET` | `/api/v1/categories` | List all categories |
| `GET` | `/api/v1/categories/{id}` | Get category by ID |
| `DELETE` | `/api/v1/categories/{id}` | Delete a category |

#### POST `/api/v1/categories`

**Request Body**
```json
{
  "name": "Electronics"
}
```

**Response** `201 Created`
```json
{
  "success": true,
  "message": "...",
  "data": {
    "name": "Electronics"
  }
}
```

---

### Products

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/products` | List all products |
| `GET` | `/api/v1/products/{id}` | Get product by ID |
| `POST` | `/api/v1/products` | Create a new product |
| `DELETE` | `/api/v1/products/{id}` | Delete a product |
| `GET` | `/api/v1/products/search?categoryName=` | Search products by category name |
| `GET` | `/api/v1/products/categories` | List all distinct category names |
| `GET` | `/api/v1/products/{id}/details` | Get product with full category details |

#### POST `/api/v1/products`

**Request Body**
```json
{
  "title": "Wireless Headphones",
  "description": "Noise-cancelling Bluetooth headphones",
  "price": 2999.99,
  "imageUrl": "https://example.com/image.jpg",
  "categoryId": 1,
  "rating": 4.5
}
```

**Response** `201 Created`
```json
{
  "success": true,
  "message": "...",
  "data": {
    "title": "Wireless Headphones",
    "description": "Noise-cancelling Bluetooth headphones",
    "price": 2999.99,
    "imageUrl": "https://example.com/image.jpg",
    "rating": 4.5
  }
}
```

#### GET `/api/v1/products/search?categoryName=Electronics`

Returns all products belonging to the given category name.

---

### Orders

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/orders` | List all orders |
| `GET` | `/api/v1/orders/{id}` | Get order by ID |
| `POST` | `/api/v1/orders` | Create a new order |
| `PUT` | `/api/v1/orders/{id}` | Update order status or items |
| `DELETE` | `/api/v1/orders/{id}` | Delete an order |
| `GET` | `/api/v1/orders/{id}/summary` | Get order summary (totals) |

#### POST `/api/v1/orders`

Creates a new order with status `PENDING`.

**Request Body**
```json
{
  "orderItems": [
    { "productId": 1, "quantity": 2 },
    { "productId": 3, "quantity": 1 }
  ]
}
```

**Response** `201 Created`
```json
{
  "success": true,
  "message": "...",
  "data": {
    "id": 10,
    "orderStatus": "PENDING",
    "items": [
      {
        "productId": 1,
        "productName": "Wireless Headphones",
        "productPrice": 2999.99,
        "productImage": "https://...",
        "quantity": 2,
        "subTotal": 5999.98
      }
    ],
    "createdAt": "2026-06-07T10:00:00",
    "updatedAt": "2026-06-07T10:00:00"
  }
}
```

#### PUT `/api/v1/orders/{id}`

Updates the order status and/or modifies order items. Supported item actions: `ADD`, `REMOVE`, `INCREMENT`, `DECREMENT`.

**Request Body**
```json
{
  "status": "SHIPPED",
  "orderItems": [
    { "productId": 1, "quantity": 1, "action": "INCREMENT" },
    { "productId": 3, "quantity": 0, "action": "REMOVE" }
  ]
}
```

**Order Status Values:** `PENDING`, `SHIPPED`, `DELIVERED`, `CANCELLED`

**Item Actions:**
| Action | Behaviour |
|---|---|
| `ADD` | Add a new product to the order |
| `REMOVE` | Remove a product from the order |
| `INCREMENT` | Increase quantity by the given amount |
| `DECREMENT` | Decrease quantity; removes item if quantity reaches 0 |

#### GET `/api/v1/orders/{id}/summary`

**Response** `200 OK`
```json
{
  "success": true,
  "data": {
    "id": 10,
    "status": "PENDING",
    "items": [...],
    "totalItems": 3,
    "totalPrice": 8999.97,
    "createdAt": "2026-06-07T10:00:00",
    "updatedAt": "2026-06-07T10:00:00"
  }
}
```

---

### Reviews

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/reviews` | List all reviews |
| `GET` | `/api/v1/reviews/{id}` | Get review by ID |
| `POST` | `/api/v1/reviews` | Create a new review |
| `DELETE` | `/api/v1/reviews/{id}` | Delete a review |
| `GET` | `/api/v1/reviews/product/{productId}` | Get all reviews for a product |
| `GET` | `/api/v1/reviews/order/{orderId}` | Get all reviews for an order |

#### POST `/api/v1/reviews`

**Request Body**
```json
{
  "productId": 1,
  "orderId": 10,
  "rating": 4.5,
  "comment": "Great product, fast delivery!"
}
```

**Response** `201 Created`
```json
{
  "success": true,
  "data": {
    "id": 5,
    "productId": 1,
    "orderId": 10,
    "rating": 4.5,
    "comment": "Great product, fast delivery!",
    "createdAt": "2026-06-07T10:00:00"
  }
}
```

---

## Data Models

### Entity Hierarchy

All entities extend `BaseEntity` which provides:

| Field | Type | Description |
|---|---|---|
| `id` | Long | Auto-increment primary key |
| `createdAt` | LocalDateTime | Set automatically on insert |
| `updatedAt` | LocalDateTime | Set automatically on update |
| `deletedAt` | LocalDateTime | Soft delete timestamp (null = active) |

### Entities

**Category** — `categories` table
- `name` (String, NOT NULL)

**Product** — `products` table
- `title` (String, NOT NULL)
- `description` (String, TEXT)
- `price` (BigDecimal, NOT NULL)
- `imageUrl` (String)
- `rating` (BigDecimal, NOT NULL)
- `category` → ManyToOne → Category

**Order** — `orders` table
- `orderStatus` (OrderStatus enum)

**OrderProductMapping** — `order_product_mapping` table
- `order` → ManyToOne → Order
- `product` → ManyToOne → Product
- `quantity` (Integer, NOT NULL)

**Review** — `reviews` table
- `comment` (String, TEXT)
- `rating` (BigDecimal, precision=3, scale=1)
- `product` → ManyToOne → Product
- `order` → ManyToOne → Order

---

## Response Format

All API responses are wrapped in a standard envelope:

```json
{
  "success": true | false,
  "message": "Human-readable message",
  "error": "Error detail (only on failure)",
  "data": { ... }
}
```

---

## Error Handling

| Scenario | HTTP Status |
|---|---|
| Resource not found (by ID) | `404 Not Found` |
| Malformed JSON body | `400 Bad Request` |
| Path variable type mismatch | `400 Bad Request` |
| Unhandled server error | `500 Internal Server Error` |

All errors follow the same [response envelope](#response-format) with `success: false` and a populated `error` field.

---

## Key Features

- **Soft Deletes** — All entities support soft deletion via a `deleted_at` column. Deleted records are transparently filtered from all queries using Hibernate `@SQLDelete` and `@SQLRestriction`.

- **JPA Auditing** — `createdAt` and `updatedAt` are automatically managed via `@EnableJpaAuditing`.

- **N+1 Query Prevention** — Product and order-item queries use `JOIN FETCH` and batch `findAllById` lookups to avoid N+1 database round-trips.

- **Flyway Migrations** — Database schema is version-controlled; `ddl-auto: validate` ensures Hibernate never silently alters the schema.

- **Consistent API Responses** — Every endpoint returns a typed `ApiResponse<T>` envelope for uniform client parsing.

- **New Relic Monitoring** — The application is instrumented with the New Relic Java agent for production observability (APM, tracing, metrics).

- **Environment-based Config** — Credentials and sensitive values are loaded from a `.env` file at runtime, keeping secrets out of source control.
