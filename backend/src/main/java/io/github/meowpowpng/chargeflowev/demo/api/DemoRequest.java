package io.github.meowpowpng.chargeflowev.demo.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DemoRequest(
        @NotNull
        @Min(1)
        Integer rideSamples,

        @NotNull
        @Min(1)
        Integer chargingSamples
) {}
