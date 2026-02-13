# ChargeFlow EV ⚡ — Charging Operations Platform

[![Backend CI](https://github.com/meowpow-png/charge-flow-ev/actions/workflows/ci.yml/badge.svg)](https://github.com/meowpow-png/charge-flow-ev/actions/workflows/ci.yml)
[![codecov](https://codecov.io/github/meowpow-png/charge-flow-ev/graph/badge.svg?token=EUCUTL2ET9)](https://codecov.io/github/meowpow-png/charge-flow-ev)

This project is a backend system that models how e-bike usage generates energy consumption, charging activity, and billable revenue.

The system ingests ride and charging telemetry, persists it as immutable session data, and derives billing records and aggregated analytics in a deterministic and explainable way.

- Telemetry is ingested via REST APIs
- Usage is modeled as immutable ride and charging sessions
- Billing and metrics are derived when sessions are finalized
- Aggregated analytics are exposed via read-only endpoints

## What Problem This Solves

Companies operating shared e-bikes or EV assets need to understand:

- How much energy is consumed per ride
- How charging infrastructure is utilized
- How usage translates into revenue

This project demonstrates how those concerns can be modeled, measured, and billed in a clean backend system.

## System Components

- **Backend Service (Java / Spring Boot)**
  Handles session lifecycles, billing logic, and analytics.

- **Relational Database**
  Persists sessions, telemetry, and billing records.

## Demo Flow

1. A ride session is started
2. Ride telemetry is periodically recorded
3. The ride completes at a charging station
4. A charging session completes
5. Billing and metrics are calculated automatically

## Documentation

- [System Concept](docs/system-concept.md) — high-level conceptual model and system behavior
- [Domain Concepts](docs/domain-concept.md) — core domain language and models
- [Development Journal](docs/dev-journal/journal-index.md) — implementation notes and decisions
- [Project Specification](docs/project-spec.md) — scope, constraints, success criteria, and technology choices

## Inspecting the System

The backend can be inspected in three ways:

1. **Swagger UI**
   All APIs are documented and can be exercised manually.

2. **Pre-seeded demo data**
   The application starts with a complete example ride and charging session,
   allowing analytics and billing endpoints to be inspected immediately.


## Running the Demo

> Detailed setup instructions will be added once implementation begins.
