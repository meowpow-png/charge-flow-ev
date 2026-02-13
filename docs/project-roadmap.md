---
title: "Project Roadmap"
tags: [docs, roadmap]
---

## Day 1 — Project Definition & Documentation

**Goal:**

The project intent, scope, and constraints are fully defined and documented.

**To-Do:**

- Define and lock project intent and success criteria
- Define explicit scope and non-goals
- Draft README describing the problem, system boundaries, and evaluation paths
- Define core domain concepts and terminology
- Create initial development journal entry documenting key decisions
- Establish repository structure (modules, packages, documentation layout)

**Explicit Non-Goals:**

- No application code beyond project scaffolding
- No business logic or domain behavior
- No infrastructure setup beyond repository structure

**Definition of Done:**

- README clearly defines system purpose, non-goals, and evaluation paths
- Domain concepts are documented and consistently named
- Project scope and constraints are fixed and written down
- Repository structure supports the planned backend

**Outcome:**

Implementation can begin on Day 2 with no unresolved questions about
scope, terminology, or project direction.

## Day 2 — Backend Skeleton & Persistence Baseline

**Goal:**

The backend can accept basic session-related API calls and persist domain data reliably.

**To-Do:**

- Create Spring Boot application skeleton
- Configure PostgreSQL using Docker and JPA
- Define core domain entities *without lifecycle or business logic*
- Implement basic create/read REST endpoints for sessions
- Persist and retrieve entities successfully via JPA
- Manually verify persistence and retrieval using Swagger UI

**Explicit Non-Goals:**

- No billing calculations
- No lifecycle state transitions
- No analytics or aggregations
- No domain events beyond basic persistence callbacks

**Definition of Done:**

- Application starts without errors
- Database schema is generated or migrated successfully
- At least one session can be created and retrieved via REST

**Outcome:**

A reviewer can start the application, create a ride or charging session via Swagger,
and verify that the data is correctly stored and retrieved from the database.

## Day 3 — Telemetry, Billing & Analytics

**Goal:**

The backend supports a complete, deterministic end-to-end flow (session → telemetry → finalize → billing → analytics) that can be manually exercised via REST APIs.

**To-Do:**

- Implement minimal telemetry ingestion endpoints (backend-owned)
- Persist telemetry samples as immutable, append-only data
- Accumulate session energy totals from telemetry while sessions are active
- Implement session finalization
- Implement deterministic billing calculations based on finalized sessions
- Implement aggregation logic for usage metrics (e.g. energy, cost, counts)
- Expose read-only billing and analytics endpoints
- Add focused unit tests for billing and calculation logic
- Manually verify deterministic results via repeated executions

**Explicit Non-Goals:**

- No real-time or streaming behavior
- No telemetry aggregation beyond session totals
- No data visualization or dashboards
- No complex query optimization
- No external reporting or export formats

**Definition of Done:**

- Telemetry can be POSTed via REST APIs and is persisted immutably
- Session energy totals are derived from ingested telemetry
- Sessions can be finalized via API
- Billing runs only on finalized sessions
- Analytics endpoints are read-only and side-effect free
- Given the same persisted input data, results are deterministic
- Billing calculations are covered by unit tests
- Aggregated values are explainable from stored session and telemetry data

**Outcome:**

A reviewer can use Swagger UI to execute and inspect a full end-to-end backend flow and clearly understand how telemetry produces billing and usage metrics.

## Day 4 — CI, Integration Testing & API Demo

**Goal:**

The project can be built and validated automatically, and a reviewer can
manually execute a complete backend flow via REST endpoints.

**To-Do:**

- Implement a minimal CI pipeline (build + tests on push)
- Verify clean build and test execution in CI
- Add focused REST integration tests for critical API flows
- Ensure integration tests run against a real Spring context
- Configure and report test coverage
- Expose coverage status via badge in README
- Ensure Swagger UI exposes all relevant endpoints
- Define and validate a simple, reproducible API demo flow
  (session → telemetry → finalize → billing → analytics)
- Confirm project builds successfully from a clean clone
- Ensure deterministic behavior across environments

**Explicit Non-Goals:**

- No continuous deployment (CD)
- No new backend feature development
- No performance or load testing
- No architectural refactors
- No expansion of business scope

**Definition of Done:**

- CI pipeline builds the project and runs all tests successfully
- REST integration tests validate critical backend flows
- Coverage is generated and visible
- A reviewer can execute a full end-to-end demo flow via Swagger UI
- Project passes CI from a clean clone
- Results are reproducible and deterministic

**Outcome:**

A reviewer can clone the repository, see CI passing,
open Swagger UI, execute the demo flow manually,
and understand how telemetry produces billing and analytics results.

## Day 5 — Polish, Hardening & Demo Readiness

**Goal:**

The project can be evaluated confidently by an external reviewer with minimal setup
and guidance from the author.

**To-Do:**

- Review and finalize README and documentation for clarity and accuracy
- Clean up code structure, naming, and package boundaries
- Remove dead code, TODOs, and experimental artifacts
- Verify fresh setup from a clean clone
- Validate all supported inspection paths
- Execute and verify the full end-to-end flow

**Explicit Non-Goals:**

- No new features or functionality
- No performance tuning or optimization
- No cosmetic improvements

**Definition of Done:**

- Project can be cloned, built, and run without manual intervention
- Demo instructions are accurate and complete
- All inspection paths work as documented
- End-to-end flow produces correct and explainable results

**Outcome:**

A reviewer can run the project, inspect persisted data and analytics,
and understand the system’s behavior and value without additional explanation.