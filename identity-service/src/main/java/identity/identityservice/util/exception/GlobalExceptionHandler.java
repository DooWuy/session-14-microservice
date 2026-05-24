package identity.identityservice.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // Trả về mã lỗi 403
    public Map<String, Object> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.FORBIDDEN.value(),
                "error", "Forbidden",
                "message", ex.getMessage(),
                "path", request.getDescription(false).replace("uri=", "")
        );
    }
}
