package io.github.meowpowpng.chargeflowev.demo.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DemoConflictException extends RuntimeException {

    DemoConflictException(String message) {
        super(message);
    }
}
