package com.yilly.lims.controller;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.repository.OperatorRepository;
import com.yilly.lims.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final OperatorRepository operatorRepo;

    // 查询所有角色
    @GetMapping
    public ResponseEntity<?> all() {
        var list = roleService.findAll().stream().map(r -> Map.of(
                "roleID", r.getRoleID(),
                "roleName", r.getRoleName(),
                "permissions", r.getPermissions().stream().map(p -> p.getPermissionName()).toList()
        )).toList();
        return ResponseEntity.ok(list);
    }

    // 创建/更新角色（可一并附带权限ID集合）
    @PostMapping
    public ResponseEntity<?> upsert(@RequestParam String actor, @RequestBody Map<String, Object> body) {
        Operator op = operatorRepo.findByUsername(actor).orElse(null);
        String roleName = (String) body.get("roleName");
        Set<Long> permIds = null;
        if (body.get("permissions") instanceof List<?> l) {
            permIds = new HashSet<>();
            for (Object o : l) permIds.add(Long.valueOf(String.valueOf(o)));
        }
        var saved = roleService.upsertRole(op, roleName, permIds);
        return ResponseEntity.ok(Map.of("roleID", saved.getRoleID(), "message", "Role created/updated successfully"));
    }

    // 删除角色
    @DeleteMapping("/{roleID}")
    public ResponseEntity<?> delete(@RequestParam String actor, @PathVariable Long roleID) {
        Operator op = operatorRepo.findByUsername(actor).orElse(null);
        roleService.deleteRole(op, roleID);
        return ResponseEntity.ok(Map.of("message", "Role deleted successfully"));
    }

    // 分配单条权限给角色
    @PostMapping("/{roleID}/permissions")
    public ResponseEntity<?> assign(@RequestParam String actor, @PathVariable Long roleID, @RequestBody Map<String, Object> body) {
        Operator op = operatorRepo.findByUsername(actor).orElse(null);
        Long permissionID = Long.valueOf(String.valueOf(body.get("permissionID")));
        var saved = roleService.assignPermission(op, roleID, permissionID);
        return ResponseEntity.ok(Map.of("message", "Permissions assigned successfully to role.", "roleID", saved.getRoleID()));
    }
}
