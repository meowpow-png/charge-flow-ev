CREATE TABLE telemetry (
    id UUID PRIMARY KEY,
    session_id UUID NOT NULL,
    energy_delta NUMERIC(10, 3) NOT NULL CHECK (energy_delta >= 0),
    emitted_at TIMESTAMP NOT NULL,

    -- ensures telemetry belongs to an existing session
    -- deleting a session automatically removes its telemetry
    CONSTRAINT fk_telemetry_session
        FOREIGN KEY (session_id)
        REFERENCES session(id)
        ON DELETE CASCADE
);
