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

    // 保存登出的token
    private final Set<String> blacklist = Collections.synchronizedSet(new HashSet<>());

    //登入
    public Map<String, Object> login(String username, String rawPassword) {
        //读取用户
        var op = operatorRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        //比对密码
        if (!encoder.matches(rawPassword, op.getPassword()))
            throw new RuntimeException("用户名或密码错误");

        //组装权限
        String roleName = "USER";
        List<String> perms = List.of();

        if (op.getRole() != null) {
            roleName = op.getRole().getRoleName();
            perms = new ArrayList<>();
            for (Permission p : op.getRole().getPermissions()) {
                perms.add(p.getPermissionName());
            }
        }
        //生成token
        var token = jwt.generate(op.getUsername(), Map.of("operatorID", op.getOperatorID(), "role", roleName, "perms", perms));
        //记录操作日志
        log.log(op, "LOGIN", "operator", "login successful");
        //返回信息
        return Map.of("token", token, "operatorID", op.getOperatorID(), "role", roleName, "message", "Login successful");
    }

    //登出
    public Map<String, String> logout(String token, String actorUsername) {
        //记录已使用过的token
        blacklist.add(token);
        //找到操作员并记录日志
        operatorRepo.findByUsername(actorUsername).
                ifPresent(u -> log.log(u, "LOGOUT", "operator", "logout"));
        //返回信息
        return Map.of("message", "Logout successful");
    }

    //判断token是否被使用过
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
