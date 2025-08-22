package com.yilly.lims.controller;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.repository.OperatorRepository;
import com.yilly.lims.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissionController {

    private final PermissionService permissionService;
    private final OperatorRepository operatorRepo;

    // 创立权限（需要具备 permission:write 权限）
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> create(Authentication authentication,
                                    @RequestBody Map<String, String> body) {
        // 当前登录操作者
        Operator op = operatorRepo.findByUsername(authentication.getName()).orElse(null);
        if (op == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }

        var p = permissionService.createPermission(
                op,
                body.get("permissionName"),
                body.get("description"),
                body.get("resource"),
                body.get("action")
        );
        return ResponseEntity.ok(Map.of("permissionID", p.getPermissionID(),
                "message", "Permission created successfully."));
    }
}
