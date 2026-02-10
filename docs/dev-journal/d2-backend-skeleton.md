---
title: "Dev Journal — Day 2: Backend Skeleton & Persistence Baseline"
tags: [docs, journal]
---

## Focus

Established a minimal but real backend that starts reliably, connects to a database, and persists session data via a REST API.

## Decisions

- Introduced Flyway to make database schema explicit, reproducible, and independent of JPA.
- Kept the domain model anemic to avoid embedding lifecycle or business rules before persistence and boundaries were proven.
- Accepted JPA annotations in the domain package to avoid duplicate models and premature mapping layers.
- Ran PostgreSQL in Docker while keeping Spring Boot on the host to reduce friction while validating infrastructure integration.
- Deferred explicit HTTP error mapping, accepting default 500 responses until API contracts stabilize.

## Excluded

- No session lifecycle enforcement (e.g., active vs. finalized) until persistence and APIs are validated.
- No telemetry ingestion, billing, or analytics work, as these depend on stable session storage.
- No service-layer abstractions or DTO mapping to keep the baseline minimal and flexible.
- No comprehensive validation or error handling beyond defaults.

## Next Steps

- Introduce session lifecycle behavior (start, finalize) and enforce basic domain rules.
- Add deterministic derivations on finalized session data to support billing and analytics.