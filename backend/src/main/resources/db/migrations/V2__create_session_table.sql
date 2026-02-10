CREATE TABLE session (
    id UUID PRIMARY KEY,
    type VARCHAR(32) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP,
    energy_total NUMERIC(10, 3),
    state VARCHAR(32) NOT NULL
);
