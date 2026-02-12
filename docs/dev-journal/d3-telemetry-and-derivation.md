---
title: "Dev Journal — Day 3: Telemetry, Billing & Analytics"
tags: [docs, journal]
---

## Focus

Implemented the full backend-owned flow: telemetry ingestion, session energy accumulation, session finalization, deterministic billing, and read-only analytics — all executable via REST without a client.

## Decisions

- Modeled telemetry as immutable, append-only records linked to sessions.
- Accumulated session energy during the active phase and fixed totals at finalization.
- Enforced immutability of finalized sessions at the domain level.
- Implemented billing as a pure, deterministic calculation layer over finalized data.
- Used repository-level aggregations for analytics projections.
- Wrapped telemetry + session energy updates in a single transaction.
- Added unit tests for billing logic and integration tests for persistence boundaries.
- Enabled Jacoco coverage to make critical logic verification visible.

## Excluded

- No REST exception mapping; domain exceptions surface as generic errors.
- No concurrency safety (no optimistic locking or DB-level exclusivity constraints).
- Lifecycle invariants (e.g. single active session) enforced at service layer only.
- No authentication, rate limiting, or pagination.
- Pricing remains hardcoded.

## Next Steps

- Add CI pipeline (build + tests on push).
- Implement minimal simulation client to automate the full flow.