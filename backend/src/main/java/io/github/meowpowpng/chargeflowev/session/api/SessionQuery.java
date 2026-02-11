package io.github.meowpowpng.chargeflowev.session.api;

import java.util.Optional;
import java.util.UUID;

public interface SessionQuery {

    Optional<FinalizedSession> findFinalizedById(UUID sessionId);
}
