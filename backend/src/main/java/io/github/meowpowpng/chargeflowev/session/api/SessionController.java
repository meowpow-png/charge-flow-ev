package io.github.meowpowpng.chargeflowev.session.api;

import io.github.meowpowpng.chargeflowev.session.domain.Session;
import io.github.meowpowpng.chargeflowev.session.domain.SessionType;
import io.github.meowpowpng.chargeflowev.session.internal.SessionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
public final class SessionController {

    private final SessionService service;

    SessionController(SessionService service) {
        Objects.requireNonNull(service, "service must not be null");
        this.service = service;
    }

    @PostMapping
    public Session createSession(@RequestParam SessionType type) {
        return service.createSession(type);
    }

    @GetMapping
    public List<Session> getAllSessions() {
        return service.getAllSessions();
    }

    @GetMapping("/{id}")
    public Session getSession(@PathVariable UUID id) {
        return service.getSession(id);
    }

    @PostMapping("/{id}/finalize")
    public Session finalizeSession(@PathVariable UUID id) {
        return service.finalizeAndSave(id);
    }
}
