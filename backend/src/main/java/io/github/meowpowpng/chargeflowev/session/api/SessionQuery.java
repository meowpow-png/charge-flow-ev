package io.github.meowpowpng.chargeflowev.session.api;

import io.github.meowpowpng.chargeflowev.session.domain.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionQuery {

    Optional<Session> findFinalizedById(UUID sessionId);
}
