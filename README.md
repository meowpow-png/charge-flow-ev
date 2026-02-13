# ChargeFlow EV ⚡ — Charging Operations Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff)](#)
[![Postgres](https://img.shields.io/badge/Postgres-%23316192.svg?logo=postgresql&logoColor=white)](#)
[![Backend CI](https://github.com/meowpow-png/charge-flow-ev/actions/workflows/ci.yml/badge.svg)](https://github.com/meowpow-png/charge-flow-ev/actions/workflows/ci.yml)
[![codecov](https://codecov.io/github/meowpow-png/charge-flow-ev/graph/badge.svg?token=EUCUTL2ET9)](https://codecov.io/github/meowpow-png/charge-flow-ev)


ChargeFlow EV is a deliberately scoped backend system developed within a fixed 5-day timebox. It models how e-bike usage generates energy consumption, charging activity, and billable revenue.

## Purpose

This project demonstrates disciplined backend modeling within a constrained scope.

It emphasizes:

- Clear ownership of domain responsibilities
- Explicit session state transitions
- Deterministic derivation of billing from immutable data
- Pragmatic scope control and trade-off decisions

## How It Works

- Telemetry is ingested via REST APIs
- Sessions manage lifecycle and immutability
- Billing is calculated from finalized energy totals
- Analytics exposes read-only aggregates

## Architecture

The system is implemented as a modular monolith. The codebase is organized
by domain context rather than technical layers.

```
src/main/java/...  
├── session  
│ ├── api  
│ ├── domain  
│ └── internal  
├── telemetry  
├── billing  
├── analytics  
└── demo
```

Each context owns its models and business logic. Cross-context interaction
is explicit and limited to well-defined boundaries.

## Components

- **Backend Service (Java / Spring Boot)**
  A single deployable service responsible for session lifecycle management,
  billing logic, and analytics.

- **PostgreSQL Database**
  Persists sessions, telemetry, and derived billing data.

## Configuration

Billing prices are externalized via application properties:

```properties
chargeflow.billing.ride-price=0.30
chargeflow.billing.charging-cost=0.10
```

Values are validated at startup and can be adjusted without modifying domain code.

## Quick Start

**Requirements**

- Java 21+
- Docker (for PostgreSQL)

**Start Database**

```sh
docker compose up -d
```

or

```sh
./gradlew composeUp
```

> [!NOTE]
> Docker Compose also starts Adminer in a separate container.

**Start Backend**

```sh
./gradlew bootRun
```

**Run Demo**

1. Open Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui/index.html)
2. Execute `/demo/run` in `demo-controller` with desired telemetry sample counts
3. Inspect the returned session ids and billing results
4. (Optional) Execute `/analytics/summary` to inspect aggregated metrics

**Run Tests**

Run all verification tasks and generate coverage:

```sh
./gradlew clean check coverage
```

Individual tasks:

```
./gradlew test
./gradlew integrationTest
./gradlew coverage
```

## Documentation

- [System Concept](docs/system-concept.md) — conceptual lifecycle and data flow
- [Domain Concepts](docs/domain-concept.md) — bounded contexts and responsibilities
- [Project Specification](docs/project-spec.md) — scope, constraints, and success criteria
- [Development Journal](docs/dev-journal/journal-index.md) — key implementation decisions and trade-offs

---

Licensed under the MIT License.