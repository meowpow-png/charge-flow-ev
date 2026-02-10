package io.github.meowpowpng.chargeflowev.session.internal;

import io.github.meowpowpng.chargeflowev.session.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {}
