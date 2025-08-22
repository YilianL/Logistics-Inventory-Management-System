package com.yilly.lims.controller;

import com.yilly.lims.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/operator", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthService auth;

    // 登录
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(auth.login(body.get("username"), body.get("password")));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message","用户名或密码错误"));
        }
    }

    // 登出
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    Authentication authentication,
                                    @RequestBody(required = false) Map<String, String> body) {

        String header = request.getHeader("Authorization");
        String token = (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
        if ((token == null || token.isBlank()) && body != null) {
            token = body.get("token");
        }
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(401).body(Map.of("message","Unauthorized"));
        }

        String actorUsername = authentication.getName(); // 当前登录用户
        return ResponseEntity.ok(auth.logout(token, actorUsername));
    }
}
