---
title: "Domain Concepts"
tags: [docs, ddd]
---

## Session Context

The Session context owns **ride and charging sessions** and their lifecycle. It is the **source of truth** for when a session exists, whether it is active, and when it becomes immutable.

### Language

- **Session**: a time-bounded EV activity
- **Ride Session**: consumes energy
- **Charging Session**: adds energy
- **Session ID**: identifies a session
- **Session State**: marks lifecycle status
- **Active**: ongoing, accepts telemetry
- **Finalized**: completed, read-only

### Responsibilities

- Start and end ride sessions
- Start and end charging sessions
- Track start/end timestamps
- Accept energy input while active
- Finalize sessions and fix energy totals
- Expose finalized sessions to downstream logic

### Business Rules

- Only **one active session** may exist at a time
- Sessions always start as **Active**
- Telemetry is accepted **only** for active sessions
- A session can be finalized **once**
- Finalized sessions are **immutable**
- Energy totals are fixed at finalization

### Boundary

Other contexts may:
- Read finalized session data

Other contexts may not:
- Change session state
- Control session lifecycle

---

## Telemetry Context

The Telemetry context captures **raw EV measurements** during active sessions.
It stores input data only and does not interpret it.

### Language

- **Telemetry**: a single measurement from the EV
- **Telemetry Sample**: timestamped telemetry data
- **Telemetry ID**: identifies a telemetry record
- **Energy Delta**: incremental energy change
- **Emission Time**: when the sample was produced

### Responsibilities

- Accept telemetry from the EV
- Verify a target session is active
- Persist telemetry as append-only data
- Link telemetry to its session

### Business Rules

- Telemetry is accepted **only** for active sessions
- Persisted telemetry is **immutable**
- Telemetry does not change session state
- No aggregation or billing logic lives here

### Boundary

Other contexts may:
- Read telemetry data

Other contexts may not:
- Modify telemetry
- Attach business meaning to telemetry

---

## Billing Context

The Billing context calculates **cost** from finalized session data.
It applies fixed pricing rules to energy totals and returns derived results.

### Language

- **Billing Record**: calculated cost output
- **Pricing Rule**: energy → cost mapping
- **Unit Price**: price per energy unit
- **Billable Energy**: finalized energy total

### Responsibilities

- Read finalized session energy totals
- Apply pricing rules
- Calculate cost deterministically
- Expose billing results

### Business Rules

- Billing runs **only** on finalized sessions
- Calculations are pure and repeatable
- Same input always produces the same output
- Billing does not change session or telemetry data
- Pricing rules are explicit and fixed

### Boundary

Billing may:
- Read session data
- Read pricing configuration

Billing may not:
- Control session lifecycle
- Modify domain data
- Store billing as primary state

---

## Analytics (Read-Only Projection)

Analytics exposes read-only aggregates over finalized data.
It does not own domain state and has no business authority.

### Inputs

- Finalized session data
- Derived billing results

### Responsibilities

- Aggregate session data
- Aggregate billing results
- Expose metrics via read-only APIs

### Constraints

- Operates only on finalized data
- All results are fully derivable
- No upstream behavior is affected
- No state is persisted

### Boundary

Analytics may:
- Read session data
- Read billing results

Analytics may not:
- Modify domain data
- Enforce rules
- Participate in workflows

---