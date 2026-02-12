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
- Repository structure supports the planned backend and simulation client

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

The backend supports a complete, deterministic end-to-end flow (session → telemetry → finalize → billing → analytics) that can be manually exercised via REST APIs, without client.

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

- No simulation client
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

## Day 4 — CI/CD Pipeline & Simulation Client

**Goal:**

The project can be built and validated automatically, and an optional LibGDX-based simulation client can execute the full backend flow without any manual API interaction.

**To-Do:**

- Implement a minimal CI pipeline (build + tests on push)
- Verify clean build and test execution in CI
- Create a minimal LibGDX simulation client
- Implement HTTP communication with backend REST endpoints
- Automate session start, telemetry emission, session finalization, and billing flow
- Execute one complete ride → charging → billing flow via the client
- Verify client-generated data via backend billing and analytics endpoints

**Explicit Non-Goals:**

- No backend feature development
- No gameplay mechanics or UI polish
- No real-time synchronization or streaming
- No bidirectional communication
- No client-side business logic or calculations
- No performance or load testing

**Definition of Done:**

- CI pipeline builds the project and runs tests successfully
- Project passes CI from a clean clone
- Simulation client runs independently of the backend
- Client uses the same REST APIs available via Swagger UI
- Backend analytics and billing reflect client-generated telemetry
- No backend logic depends on the simulation client

**Outcome:**

A reviewer can clone the repository, see CI passing, optionally run the simulation client, and observe a complete, automated end-to-end flow on the backend.

## Day 5 — Polish, Hardening & Demo Readiness

**Goal:**

The project can be evaluated confidently by an external reviewer with minimal setup
and no guidance from the author.

**To-Do:**

- Review and finalize README and documentation for clarity and accuracy
- Clean up code structure, naming, and package boundaries
- Remove dead code, TODOs, and experimental artifacts
- Verify fresh setup from a clean clone
- Validate all supported inspection paths
- Execute and verify the full end-to-end flow

**Explicit Non-Goals:**

- No new features or functionality
- No architectural refactors
- No performance tuning or optimization
- No cosmetic UI improvements

**Definition of Done:**

- Project can be cloned, built, and run without manual intervention
- Demo instructions are accurate and complete
- All inspection paths work as documented
- End-to-end flow produces correct and explainable results

**Outcome:**

A reviewer can run the project, inspect persisted data and analytics,
and understand the system’s behavior and value without additional explanation.