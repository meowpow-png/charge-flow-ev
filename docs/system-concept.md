---
title: "System Concept"
tags: [docs, concept]
---

> This document describes the high-level conceptual flow of the ChargeFlow EV system.
> It exists to clarify domain behavior and data lifecycles and does not prescribe
> implementation details, APIs, or architectural patterns.

---

![[diagram.svg]]

---

## Concept Overview

At a high level, the system models a real e-bike going through a simple ride and charging cycle:

- The **e-bike starts a ride** and **consumes energy** as it is used
- During the ride, the **system tracks how much energy is consumed** over time
- When the **ride ends**, the total energy used is fixed and recorded
- The e-bike is then **charged** at a charging station, adding energy back to the battery
- **When charging ends**, the total energy delivered is fixed and recorded
- After **riding and charging are complete**, costs and usage metrics are calculated from recorded data

---

## Design Principles

- **Immutability:** sessions are finalized and never modified afterward
- **Determinism:** derived results are predictable and reproducible
- **Backend-first modeling:** no assumptions about UI or presentation

---

## Session Lifecycle

### Initial State

- The system has no active ride or charging sessions
- No energy consumption or billing data exists
- The EV is considered idle

At this stage, the system holds only configuration and reference data.

### Ride Session

A ride session represents a period of vehicle usage during which energy is consumed.

When a ride session starts, the system records a unique ride identifier, a start timestamp,
and an association to the EV and rider. At this point, the session exists but contains no
final usage totals and is considered **active and mutable**.

While the ride is active, telemetry samples are emitted periodically. Each sample represents
incremental usage information (such as elapsed time, distance, or energy consumed) and
contributes to the accumulated energy consumption for the session. Telemetry is treated
strictly as input data; no billing or analytics are calculated while the session is active.

When the ride ends, the session is finalized. Total energy consumed is calculated and fixed,
and the session becomes **immutable**. No further telemetry can be added. At this point,
the ride session represents a complete historical record of usage and provides sufficient
input for downstream billing and analytics derivations.

### Charging Session

A charging session represents a period during which energy is replenished at a charging station.

After a ride completes, the EV may enter a charging session. When the session starts, the
system records a unique charging session identifier, a start timestamp, and an association
to the EV and charging station. The session is considered **active and mutable** at this
point.

While charging is active, energy is added to the EV at a deterministic rate. The system
tracks elapsed charging time and total energy delivered. Charging data is accumulated
incrementally, but no billing is finalized while the session remains active.

When charging ends, the session is finalized. Total energy delivered is fixed, and the
charging session becomes **immutable**. At this point, the system has a complete record
of energy replenishment and all inputs required for billing derivation are available.

---

## Billing & Analytics

Once ride and charging sessions are complete, billing and analytics are derived from the
recorded session data.

Billing records are **derived**, not stored as primary facts. Costs are calculated
deterministically based on energy consumed, energy charged, and predefined pricing rules.
Given the same session data, the same billing result is always produced, and billing does
not modify or affect the underlying session records.

After sessions are finalized, aggregated analytics become available. These include metrics
such as total energy consumed, total energy charged, total revenue, counts of ride and
charging sessions, and simple averages. All analytics are read-only, derived from immutable
data, and explainable by tracing results back to individual sessions.

---

## Summary

The ChargeFlow EV system models EV usage as a sequence of simple, deterministic steps:
ride → charge → derive.

By keeping behavior explicit and data immutable, the system enables clear billing,
trustworthy analytics, and a backend design that is easy to reason about, test, and evaluate.

---