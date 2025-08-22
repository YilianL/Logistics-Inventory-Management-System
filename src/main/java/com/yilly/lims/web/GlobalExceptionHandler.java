package com.yilly.lims.web;

import com.yilly.lims.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.SignatureException;
import java.time.OffsetDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 统一构造错误响应体
    private Map<String, Object> body(int status, String message, String path) {
        return Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "status", status,
                "message", message,
                "path", path
        );
    }

    // 显式 ResponseStatusException（你代码里有用到）
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleRSE(ResponseStatusException ex, HttpServletRequest req) {
        var map = body(ex.getStatusCode().value(), Optional.ofNullable(ex.getReason()).orElse(ex.getMessage()), req.getRequestURI());
        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(map);
    }

    // 常见 400：参数/状态非法
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(RuntimeException ex, HttpServletRequest req) {
        return body(400, ex.getMessage(), req.getRequestURI());
    }

    // JSON 解析失败 → 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> unreadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return body(400, "Malformed JSON request", req.getRequestURI());
    }

    // 缺少必需的 query 参数 → 400
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> missingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        return body(400, "Missing parameter: " + ex.getParameterName(), req.getRequestURI());
    }

    // JSR-380/Bean Validation 校验失败 → 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        var map = new LinkedHashMap<String, Object>();
        map.put("timestamp", OffsetDateTime.now().toString());
        map.put("status", 400);
        map.put("message", "Validation failed");
        map.put("errors", errors);
        map.put("path", req.getRequestURI());
        return map;
    }

    // 404 场景（如果你抛的是 JPA 的 NotFound）
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> notFound(EntityNotFoundException ex, HttpServletRequest req) {
        return body(404, ex.getMessage(), req.getRequestURI());
    }

    // 401：过期
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> jwtExpired(ExpiredJwtException ex, HttpServletRequest req) {
        return body(401, "TOKEN_EXPIRED", req.getRequestURI());
    }

    // 401：签名错误
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> jwtBadSignature(SignatureException ex, HttpServletRequest req) {
        return body(401, "BAD_SIGNATURE", req.getRequestURI());
    }

    // 401：格式/不支持
    @ExceptionHandler({ MalformedJwtException.class, UnsupportedJwtException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> jwtInvalid(RuntimeException ex, HttpServletRequest req) {
        return body(401, "TOKEN_INVALID", req.getRequestURI());
    }

    // 兜底 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> serverError(Exception ex, HttpServletRequest req) {
        return body(500, "Internal Server Error", req.getRequestURI());
    }
}
