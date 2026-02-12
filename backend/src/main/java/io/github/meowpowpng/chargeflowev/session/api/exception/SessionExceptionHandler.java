package io.github.meowpowpng.chargeflowev.session.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@SuppressWarnings("unused")
public final class SessionExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SessionNotFoundException.class)
    ErrorResponse handleSessionNotFound(SessionNotFoundException ex) {
        return new ErrorResponse("SESSION_NOT_FOUND", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SessionStateViolationException.class)
    ErrorResponse handleStateViolation(SessionStateViolationException ex) {
        return new ErrorResponse("SESSION_STATE_VIOLATION", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    ErrorResponse handleBadRequest(IllegalArgumentException ex) {
        return new ErrorResponse("BAD_REQUEST", ex.getMessage());
    }
}
