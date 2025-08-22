package com.yilly.lims.controller;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.repository.OperatorRepository;
import com.yilly.lims.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleController {

    private final RoleService roleService;
    private final OperatorRepository operatorRepo;

    // 查询所有角色（需要读取权限）
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> all() {
        var list = roleService.findAll().stream().map(r -> Map.of(
                "roleID", r.getRoleID(),
                "roleName", r.getRoleName(),
                "permissions", r.getPermissions().stream().map(p -> p.getPermissionName()).toList()
        )).toList();
        return ResponseEntity.ok(list);
    }

    // 创建/更新角色（需要写权限）
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> upsert(Authentication authentication,
                                    @RequestBody Map<String, Object> body) {
        // 当前登录用户
        Operator op = operatorRepo.findByUsername(authentication.getName()).orElse(null);
        if (op == null) return ResponseEntity.status(401).body(Map.of("message","Unauthorized"));

        String roleName = (String) body.get("roleName");

        // 解析权限ID集合
        Set<Long> permIds = null;
        Object raw = body.get("permissions");
        if (raw instanceof List<?> l) {
            permIds = new HashSet<>();
            for (Object o : l) permIds.add(Long.valueOf(String.valueOf(o)));
        }

        var saved = roleService.upsertRole(op, roleName, permIds);
        return ResponseEntity.ok(Map.of("roleID", saved.getRoleID(), "message", "Role created/updated successfully"));
    }

    // 删除角色（需要删除权限）
    @DeleteMapping("/{roleID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(Authentication authentication,
                                    @PathVariable Long roleID) {
        Operator op = operatorRepo.findByUsername(authentication.getName()).orElse(null);
        if (op == null) return ResponseEntity.status(401).body(Map.of("message","Unauthorized"));

        roleService.deleteRole(op, roleID);
        return ResponseEntity.ok(Map.of("message", "Role deleted successfully"));
    }

    // 分配单条权限给角色（需要分配权限）
    @PostMapping(value = "/{roleID}/permissions", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('role:assign')")
    public ResponseEntity<?> assign(Authentication authentication,
                                    @PathVariable Long roleID,
                                    @RequestBody Map<String, Object> body) {
        Operator op = operatorRepo.findByUsername(authentication.getName()).orElse(null);
        if (op == null) return ResponseEntity.status(401).body(Map.of("message","Unauthorized"));

        Long permissionID = Long.valueOf(String.valueOf(body.get("permissionID")));
        var saved = roleService.assignPermission(op, roleID, permissionID);
        return ResponseEntity.ok(Map.of("message", "Permissions assigned successfully to role.", "roleID", saved.getRoleID()));
    }
}
