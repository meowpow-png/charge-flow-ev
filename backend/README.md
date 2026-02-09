---
title: Backend Service
tags: [docs, readme]
---

The backend service responsible for ingesting telemetry, modeling charging sessions,
calculating billing, and exposing analytics.

This service is the core of the project and contains all domain logic, persistence,
and aggregation behavior.

## Role

- Ingests ride and charging telemetry via REST APIs
- Models immutable session lifecycles (ride → charging)
- Derives deterministic energy usage, billing, and metrics
- Exposes read-only analytics and metrics for inspection

## Responsibilities

- Domain modeling and business logic
- Persistence of session data as immutable records
- Deterministic billing and metric derivation
- API contract definition and documentation

## Scope

- Single logical tenant
- Deterministic pricing model
- One complete end-to-end flow

## Integration Notes

- Telemetry is produced by an external simulation client
- The backend does not depend on the simulation client to function
- APIs can be exercised manually via Swagger UI

## Technology

- Java
- Spring Boot
- RESTful APIs
- SQL-based persistence