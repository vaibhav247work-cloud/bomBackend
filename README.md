# Part & BOM Management Service

## Overview

This project implements a small **Part and Bill of Materials (BOM) management system**, inspired by real-life enterprise PLM and system integration scenarios.

The solution is split into two independent applications:
- **Backend**: Spring Boot REST API
- **Frontend**: Vue 3 application (separate project)

This repository contains the **backend application**.

---

## Backend – Spring Boot Application

### Features

#### Part Management
- Create and update Parts
- Search Parts by part number or name
- Retrieve Part details
- Automatic part number generation if not provided  
  (Format: `PRT-000001`)

#### BOM Management
- Create and remove parent–child BOM links
- Retrieve BOM structure starting from a root Part
- Configurable expand depth (default = 1)
- Maximum expand depth enforced with clear error messages

#### Audit Logging
Audit logs are generated automatically for:
- Part creation
- Part update
- BOM link creation
- BOM link removal

Audit logs can be retrieved per Part via REST API.

---

## Technology Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 In-Memory Database
- Maven

---

## Architecture

The backend follows a layered architecture:

Controller → Service → Repository → Database

yaml
Copy code

Key design principles:
- Clear separation of concerns
- Business rules enforced in the service layer
- RESTful API design
- Enterprise-style BOM modeling using explicit link entities
- Dedicated business sequence table for identifier generation

---

## Data Storage

- H2 in-memory database is used for simplicity
- Schema is created automatically on startup
- Sample data is generated automatically to allow quick testing

### Sample Data
On application startup, the system creates:
- 1 root Part
- 2–3 BOM levels
- Approximately 15–20 total Parts
- A meaningful BOM hierarchy for testing expand behavior

---

## Business Rules

### Part Number Generation
- If `partNumber` is not provided, the backend generates it automatically
- A dedicated **BusinessSequence** table is used (PLM-style)
- Ensures uniqueness across restarts and avoids collisions

### BOM Expand Rule
- BOM expansion is limited by a maximum depth
- If the limit is exceeded, a clear error message is returned

Example error:
```json
{
  "message": "BOM expansion limit exceeded (max depth = 5)"
}
Running the Application
Prerequisites
Java 17+

Maven

Start Backend
bash
Copy code
mvn spring-boot:run
Default Port
Backend: http://localhost:8080

H2 Database Console
The H2 console is enabled for inspection and testing.

URL: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:bomdb

Username: sa

Password: (empty)

REST API Overview
Part APIs
Method	Endpoint	Description
POST	/api/parts	Create a part
PUT	/api/parts/{id}	Update a part
GET	/api/parts/{id}	Get part details
GET	/api/parts/search?query=	Search parts

BOM APIs
Method	Endpoint	Description
GET	/api/bom/{rootPartId}	Get BOM (default depth = 1)
GET	/api/bom/{rootPartId}?depth=N	Get BOM with depth
POST	/api/bom/link	Create BOM link
DELETE	/api/bom/link	Remove BOM link

Audit APIs
Method	Endpoint	Description
GET	/api/audit/part/{partId}	Get audit logs for a part

Error Handling
Global error handling is implemented using @ControllerAdvice

Business validation errors return clean JSON responses

Database and technical errors are not exposed to clients
