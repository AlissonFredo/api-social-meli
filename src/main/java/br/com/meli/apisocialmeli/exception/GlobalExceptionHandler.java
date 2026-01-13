package br.com.meli.apisocialmeli.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", ex.getStatusCode().value());
        String error = (ex.getStatusCode() instanceof HttpStatus httpStatus) ? httpStatus.getReasonPhrase() : "Error";
        body.put("error", error);
        body.put("message", ex.getReason());
        body.put("path", request.getRequestURI());
        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("message", "Erro de validação nos campos.");
        body.put("path", request.getRequestURI());

        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream().map(fe -> {
            Map<String, Object> e = new LinkedHashMap<>();
            e.put("field", fe.getField());
            e.put("message", fe.getDefaultMessage());
            if (fe.getRejectedValue() != null) e.put("rejectedValue", fe.getRejectedValue());
            return e;
        }).toList();
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }
}
