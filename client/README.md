---
title: Simulation Client
tags: [docs, readme]
---

A minimal client used to generate ride and charging telemetry for the backend system.

This module exists solely to support backend development and demonstration.
All business logic, persistence, and analytics live in the backend service.

---

## Role

- Simulates a single e-bike ride using deterministic rules
- Sends ride and charging telemetry to the backend via REST APIs
- Enables end-to-end validation of backend behavior

---

## Scope

- One predefined route
- One charging station
- One complete ride → charge flow

---

## Design Notes

- The client emits telemetry only and contains no business logic
- Communication with the backend is one-way (client → backend)
- The client is optional and not required to evaluate the backend

---

## Technology

- LibGDX
- HTTP-based communication with the backend

---