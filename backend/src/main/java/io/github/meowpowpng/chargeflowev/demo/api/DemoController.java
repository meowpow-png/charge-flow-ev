package io.github.meowpowpng.chargeflowev.demo.api;

import io.github.meowpowpng.chargeflowev.demo.application.DemoResponse;
import io.github.meowpowpng.chargeflowev.demo.application.DemoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/demo")
public final class DemoController {

    private final DemoService demoService;

    DemoController(DemoService demoService) {
        this.demoService = Objects.requireNonNull(demoService, "demoService must not be null");
    }

    @PostMapping("/run")
    public ResponseEntity<DemoResponse> runDemo(
            @Valid @RequestBody DemoRequest request
    ) {
        DemoResponse response = demoService.run(request);
        return ResponseEntity.ok(response);
    }
}
