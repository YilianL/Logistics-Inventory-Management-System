package com.yilly.lims.controller;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.repository.OperatorLogRepository;
import com.yilly.lims.repository.OperatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class OperatorLogController {

    private final OperatorLogRepository logRepo;
    private final OperatorRepository operatorRepo;

    // 全部操作日志（需要日志读取权限）
    @GetMapping
    @PreAuthorize("hasAuthority('log:read')")
    public ResponseEntity<?> listAll() {
        return ResponseEntity.ok(logRepo.findAll());
    }

    // 仅查看“我的”操作日志（已登录）
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> listMine(Authentication authentication) {
        Operator me = operatorRepo.findByUsername(authentication.getName()).orElse(null);
        if (me == null) return ResponseEntity.status(401).body(java.util.Map.of("message","Unauthorized"));
        return ResponseEntity.ok(logRepo.findByOperator_OperatorID(me.getOperatorID()));
    }
}
