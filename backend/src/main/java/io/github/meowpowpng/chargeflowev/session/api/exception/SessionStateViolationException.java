package io.github.meowpowpng.chargeflowev.session.api.exception;

import java.util.UUID;

/**
 * Thrown when a session operation violates its lifecycle rules.
 */
public final class SessionStateViolationException extends RuntimeException {

    private SessionStateViolationException(String message) {
        super(message);
    }

    /**
     * Creates an exception for attempting
     * to start a session while another is active.
     */
    public static SessionStateViolationException activeSessionAlreadyExists() {
        return new SessionStateViolationException("An active session already exists");
    }

    /**
     * Creates an exception for attempting
     * to modify a finalized session.
     */
    public static SessionStateViolationException alreadyFinalized(UUID sessionId) {
        return new SessionStateViolationException("Session already finalized (id=" + sessionId + ')');
    }

    /**
     * Creates an exception for operations
     * that require an active session.
     */
    public static SessionStateViolationException notActive(UUID sessionId) {
        return new SessionStateViolationException("Session is not active (id=" + sessionId + ')');
    }
}
