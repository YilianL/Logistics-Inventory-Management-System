package com.yilly.lims.service;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.entity.Permission;
import com.yilly.lims.repository.OperatorRepository;
import com.yilly.lims.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @RequiredArgsConstructor
public class AuthService {
    private final OperatorRepository operatorRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final LogService log;

    // 简易黑名单（登出后失效）。生产可用 Redis
    private final Set<String> blacklist = Collections.synchronizedSet(new HashSet<>());

    public Map<String, Object> login(String username, String rawPassword) {
        var op = operatorRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        if (!encoder.matches(rawPassword, op.getPassword())) throw new RuntimeException("用户名或密码错误");

        var roleName = op.getRole() != null ? op.getRole().getRoleName() : "USER";
        var perms = op.getRole() != null ? op.getRole().getPermissions().stream().map(Permission::getPermissionName).toList() : List.<String>of();
        var token = jwt.generate(op.getUsername(), Map.of("operatorID", op.getOperatorID(), "role", roleName, "perms", perms));

        log.log(op, "LOGIN", "operator", "login successful");
        return Map.of("token", token, "operatorID", op.getOperatorID(), "role", roleName, "message", "Login successful");
    }

    public Map<String, String> logout(String token, String actorUsername) {
        blacklist.add(token);
        operatorRepo.findByUsername(actorUsername).ifPresent(u -> log.log(u, "LOGOUT", "operator", "logout"));
        return Map.of("message", "Logout successful");
    }

    public boolean isBlacklisted(String token) { return blacklist.contains(token); }
}
