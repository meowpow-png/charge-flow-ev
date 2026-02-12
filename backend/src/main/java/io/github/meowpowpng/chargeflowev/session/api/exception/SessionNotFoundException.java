package io.github.meowpowpng.chargeflowev.session.api.exception;

import java.util.UUID;

/**
 * Thrown when a session with the given ID does not exist.
 */
public final class SessionNotFoundException extends RuntimeException {

    private SessionNotFoundException(UUID sessionId) {
        super("Session not found (id=" + sessionId + ')');
    }

    /**
     * Creates an exception for a
     * missing session with the given ID.
     */
    public static SessionNotFoundException forId(UUID sessionId) {
        return new SessionNotFoundException(sessionId);
    }
}
