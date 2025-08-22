package com.yilly.lims.controller;

import com.yilly.lims.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequiredArgsConstructor
@RequestMapping("/operator")
public class AuthController {
    private final AuthService auth;

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(auth.login(body.get("username"), body.get("password")));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message","用户名或密码错误"));
        }
    }

    // 用户登出
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String actor, @RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isBlank())
            return ResponseEntity.status(401).body(Map.of("message","Unauthorized"));
        return ResponseEntity.ok(auth.logout(token, actor));
    }
}
