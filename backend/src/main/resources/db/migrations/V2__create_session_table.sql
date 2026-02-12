CREATE TABLE session (
    id UUID PRIMARY KEY,
    type VARCHAR(32) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP,
    energy_total NUMERIC(10, 3) NOT NULL DEFAULT 0 CHECK (energy_total >= 0),
    state VARCHAR(32) NOT NULL,
    CHECK (ended_at IS NULL OR ended_at >= started_at)
);
