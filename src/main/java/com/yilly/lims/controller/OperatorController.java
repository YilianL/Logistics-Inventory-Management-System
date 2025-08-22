package com.yilly.lims.controller;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.repository.OperatorRepository;
import com.yilly.lims.service.OperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/operator", produces = MediaType.APPLICATION_JSON_VALUE)
public class OperatorController {

    private final OperatorService operatorService;
    private final OperatorRepository operatorRepo;

    // 用户注册（仅有写用户权限的人可操作）
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('operator:write')")
    public ResponseEntity<?> register(Authentication authentication,
                                      @RequestBody Map<String, String> body) {
        // 当前登录操作者
        Operator doer = operatorRepo.findByUsername(authentication.getName()).orElse(null);
        if (doer == null) return ResponseEntity.status(401).body(Map.of("message","Unauthorized"));

        var saved = operatorService.register(doer, body.get("username"), body.get("password"), body.get("roleName"));
        return ResponseEntity.ok(Map.of("operatorID", saved.getOperatorID(), "message", "Register successful"));
    }

    // 获取当前用户信息
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> me(Authentication authentication) {
        var me = operatorRepo.findByUsername(authentication.getName()).orElse(null);
        if (me == null) return ResponseEntity.status(401).body(Map.of("message","Unauthorized"));
        return ResponseEntity.ok(operatorService.profile(me));
    }

    // 用户信息更新（改密/换角色)
    @PutMapping(value = "/{operatorID}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('operator:write')")
    public ResponseEntity<?> update(Authentication authentication,
                                    @PathVariable Long operatorID,
                                    @RequestBody Map<String, String> body) {
        Operator doer = operatorRepo.findByUsername(authentication.getName()).orElse(null);
        if (doer == null) return ResponseEntity.status(401).body(Map.of("message","Unauthorized"));

        var saved = operatorService.update(doer, operatorID, body.get("password"), body.get("roleName"));
        return ResponseEntity.ok(Map.of("operatorID", saved.getOperatorID(), "message", "Update successful"));
    }
}
