package io.github.meowpowpng.chargeflowev.session.api;

import java.math.BigDecimal;
import java.util.UUID;

public interface SessionCommand {

    void addEnergy(UUID sessionId, BigDecimal delta);
}
