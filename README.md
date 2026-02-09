---
title: "ChargeFlow EV — Charging Operations Platform"
tags: docs
---

## Overview

This project is a backend system that models how e-bike usage generates energy consumption, charging activity, and billable revenue.

The system ingests ride and charging telemetry, persists it as immutable session data, and derives billing records and aggregated analytics in a deterministic and explainable way, using a deliberately simplified EV model that prioritizes analytical clarity over physical accuracy.

A minimal simulation client is included solely to generate realistic telemetry for demonstration and validation purposes. The backend itself is the primary focus of this project.

- Telemetry is ingested via REST APIs
- Usage is modeled as immutable ride and charging sessions
- Billing and metrics are derived when sessions are finalized
- Aggregated analytics are exposed via read-only endpoints

---

## What Problem This Solves

Companies operating shared e-bikes or EV assets need to understand:

- How much energy is consumed per ride
- How charging infrastructure is utilized
- How usage translates into revenue

This project demonstrates how those concerns can be modeled, measured, and billed in a clean backend system.

---

## System Components

- **Backend Service (Java / Spring Boot)**
  Handles session lifecycles, billing logic, and analytics.

- **Relational Database**
  Persists sessions, telemetry, and billing records.

- **Simulation Client (optional)**
  A minimal LibGDX-based tool that simulates a single e-bike ride and charging flow by emitting telemetry via the backend APIs.

---

## Demo Flow

1. A ride session is started
2. Ride telemetry is periodically recorded
3. The ride completes at a charging station
4. A charging session completes
5. Billing and metrics are calculated automatically

---

## Documentation

- [[system-concept|System Concept]] — high-level conceptual model and system behavior
- [[domain-concept|Domain Concepts]] — core domain language and models
- [[journal-index|Development Journal]] — implementation notes and decisions
- [[project-spec|Project Specification]] — scope, constraints, success criteria, and technology choices

---

## Inspecting the System

The backend can be inspected in three ways:

1. **Swagger UI**
   All APIs are documented and can be exercised manually.

2. **Pre-seeded demo data**
   The application starts with a complete example ride and charging session,
   allowing analytics and billing endpoints to be inspected immediately.

3. **Simulation client (optional)**
   A minimal LibGDX client is included to demonstrate how telemetry is generated.
   This is not required to evaluate the backend.

---

## Running the Demo

> Detailed setup instructions will be added once implementation begins.

---