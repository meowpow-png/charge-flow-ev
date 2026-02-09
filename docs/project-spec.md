---
title: "Project Specification"
tags: [docs, roadmap]
---

> This document defines the scope and constraints of the EV analytics backend demo.

## Scope & Non-Goals

This project models a **single, end-to-end EV charging analytics and billing flow**.

Included in scope:

- One rider operating a single e-bike
- One predefined route between two points
- One charging station
- A deterministic pricing model (cost derived from energy usage)
- One complete lifecycle: ride → charging → billing → analytics

This project intentionally does **not** attempt to solve the following:

- **Real EV charging protocols**
  No OCPP or hardware-level communication.

- **Real payment processing**
  No external payment providers, invoicing, or tax handling.

- **Full multi-tenant SaaS user management**
  No roles, permissions, onboarding flows, or organization hierarchies.

- **Real-time streaming or large-scale distribution**
  No message brokers, WebSockets, or scaling concerns.

- **High-fidelity gameplay or UI polish**
  The simulation client is not a game product and contains no advanced mechanics or visuals.

These exclusions are deliberate to keep focus on analytics and billing logic.

## Definition of Done

The project is considered successful if:

- A single simulated e-bike ride can be executed end-to-end
- Ride and charging telemetry is persisted correctly
- Energy usage, charging cost, and billing are calculated deterministically
- Aggregated metrics can be derived and explained from stored data
- A technical reviewer can understand the system and its value in under one minute

## Constraints

- The project is built within a fixed 5-day timeframe
- The system operates under a single logical tenant
- All pricing and calculations are transparent and reproducible

## Technology

- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Gradle
- **API Style:** RESTful APIs
- **Persistence:** SQL with PostgreSQL
- **ORM:** JPA / Hibernate
- **Architecture:** Single backend service (modular monolith; single deployable unit)
- **Local Infrastructure:** Docker + docker-compose (PostgreSQL only)
- **CI/CD:** Basic pipeline for build and tests on push
- **Frontend:** None (REST-only backend; UI intentionally out of scope)
- **Client:** Minimal LibGDX-based client used to emit ride and charging telemetry

## Testing

Given the fixed 5-day timebox, testing is approached deliberately and in phases.

During early development, APIs are manually exercised using Swagger UI to validate
domain behavior, session lifecycles, and request/response contracts while the model
is still evolving. This allows rapid feedback and avoids locking unstable behavior
into automated tests too early.

Once the end-to-end flow is validated through the simulation client and the system
behavior stabilizes, automated tests are introduced:

- Unit tests for deterministic business logic (billing, energy calculations)
- Focused integration tests for persistence and aggregation boundaries

The goal of testing in this project is not exhaustive coverage, but to ensure that the most critical and error-prone logic is verified and remains stable as the system evolves.