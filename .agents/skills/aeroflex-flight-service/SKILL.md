---
name: aeroflex-flight-service
description: Designs, implements, reviews, debugs, and production-hardens the AeroFlex Flight Service. Use when creating or modifying Flight, Airline, Airport, Aircraft, AircraftType, flight scheduling, flight status, ownership, authorization, pagination, Kafka events, database migrations, or related flight-service functionality.
---

# AeroFlex Flight Service Engineering Skill

You are working on the AeroFlex Air Ticket Booking System.

This skill defines the mandatory engineering rules for the Flight Service.

## 1. First inspect the repository

Before modifying code:

1. Inspect the existing `flight-service`.
2. Inspect `common-library`.
3. Inspect existing:
    - Airline
    - Airport
    - AircraftType
    - Aircraft
    - Flight
4. Inspect:
    - BaseEntity
    - BaseAuditEntity
    - ApiResponse
    - global exception handling
    - security configuration
    - Kafka event infrastructure
    - MapStruct configuration
    - Flyway migrations
    - logging/tracing
    - existing tests
5. Reuse existing architecture.
6. Do not create duplicate infrastructure.

Never assume that a class does not exist before searching for it.

---

# 2. Technology constraints

Use the existing AeroFlex stack:

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- SQL Server
- Flyway
- Kafka
- MapStruct
- Maven
- Docker
- Kubernetes
- SLF4J
- Logback
- OpenTelemetry/tracing

Use UUID for all identifiers.

Never introduce `Long` or `Integer` IDs.

---

# 3. Microservice boundaries

Flight Service owns flight-domain data such as:

- Airline
- Airport
- AircraftType
- Aircraft
- Flight

Do not create JPA relationships to entities owned by other microservices.

Never access another microservice's database directly.

Use REST APIs or Kafka events for cross-service communication.

---

# 4. API architecture

Use the existing:

`ApiResponse<T>`

Do not create another generic response wrapper.

Use DTOs rather than exposing JPA entities.

Typical DTOs:

- AirlineResponse
- AirportResponse
- AircraftTypeResponse
- AircraftResponse
- FlightResponse
- FlightRequest
- FlightStatusChangeRequest

Follow existing project naming conventions instead of blindly creating these classes.

---

# 5. Entity lifecycle

Use:

`active`

for soft deletion.

Do not confuse `active` with operational `status`.

Example:

```text
active = true
status = SCHEDULED