package com.yilly.lims.service;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.entity.Permission;
import com.yilly.lims.entity.Role;
import com.yilly.lims.repository.PermissionRepository;
import com.yilly.lims.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;
    private final LogService log;

    public List<Role> findAll() { return roleRepo.findAll(); }

    @Transactional
    public Role upsertRole(Operator actor, String roleName, Set<Long> permissionIds) {
        if (roleName == null || roleName.isBlank()) throw new IllegalArgumentException("roleName不能为空");

        var role = roleRepo.findByRoleName(roleName).orElseGet(() -> {
            var r = new Role();
            r.setRoleName(roleName);
            return r;
        });

        if (permissionIds != null) {
            var set = new HashSet<Permission>();
            for (Long pid : permissionIds) {
                set.add(permissionRepo.findById(pid).orElseThrow(() -> new IllegalArgumentException("权限不存在: " + pid)));
            }
            role.setPermissions(set);
        }

        var saved = roleRepo.save(role);
        log.log(actor, "UPSERT", "role", "role=" + roleName);
        return saved;
    }

    @Transactional
    public void deleteRole(Operator actor, Long roleID) {
        roleRepo.deleteById(roleID);
        log.log(actor, "DELETE", "role", "roleId=" + roleID);
    }

    @Transactional
    public Role assignPermission(Operator actor, Long roleID, Long permissionID) {
        var role = roleRepo.findById(roleID).orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        var perm = permissionRepo.findById(permissionID).orElseThrow(() -> new IllegalArgumentException("权限不存在"));
        role.getPermissions().add(perm);
        var saved = roleRepo.save(role);
        log.log(actor, "ASSIGN", "role-permission", "roleId=" + roleID + ", permId=" + permissionID);
        return saved;
    }
}
