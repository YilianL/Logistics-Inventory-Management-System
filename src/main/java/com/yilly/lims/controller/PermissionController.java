package com.yilly.lims.controller;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.repository.OperatorRepository;
import com.yilly.lims.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final OperatorRepository operatorRepo;

    // 创立权限
    @PostMapping
    public ResponseEntity<?> create(@RequestParam String actor, @RequestBody Map<String, String> body) {
        Operator op = operatorRepo.findByUsername(actor).orElse(null);
        var p = permissionService.createPermission(
                op,
                body.get("permissionName"),
                body.get("description"),
                body.get("resource"),
                body.get("action")
        );
        return ResponseEntity.ok(Map.of("permissionID", p.getPermissionID(), "message", "Permission created successfully."));
    }
}
