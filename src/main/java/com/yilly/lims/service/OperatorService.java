package com.yilly.lims.service;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.entity.Permission;
import com.yilly.lims.repository.OperatorRepository;
import com.yilly.lims.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service @RequiredArgsConstructor
public class OperatorService {
    private final OperatorRepository operatorRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final LogService log;

    public Optional<Operator> byUsername(String username) { return operatorRepo.findByUsername(username); }

    @Transactional
    public Operator register(Operator actor, String username, String rawPassword, String roleName) {
        operatorRepo.findByUsername(username).ifPresent(o -> { throw new IllegalStateException("用户名已存在"); });
        var op = new Operator();
        op.setUsername(username);
        op.setPassword(encoder.encode(rawPassword));
        if (roleName != null && !roleName.isBlank()) {
            op.setRole(roleRepo.findByRoleName(roleName).orElseThrow(() -> new IllegalArgumentException("角色不存在")));
        }
        var saved = operatorRepo.save(op);
        log.log(actor, "CREATE", "operator", "username=" + username);
        return saved;
    }

    public Map<String, Object> profile(Operator me) {
        var roleName = me.getRole() != null ? me.getRole().getRoleName() : "USER";
        var perms = me.getRole() != null ? me.getRole().getPermissions().stream().map(Permission::getPermissionName).toList() : List.of();
        return Map.of("operatorID", me.getOperatorID(), "username", me.getUsername(), "role", roleName, "permissions", perms);
    }

    @Transactional
    public Operator update(Operator actor, Long operatorID, String newPassword, String newRoleName) {
        var op = operatorRepo.findById(operatorID).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (newPassword != null && !newPassword.isBlank()) op.setPassword(encoder.encode(newPassword));
        if (newRoleName != null && !newRoleName.isBlank()) op.setRole(roleRepo.findByRoleName(newRoleName).orElseThrow(() -> new IllegalArgumentException("角色不存在")));
        var saved = operatorRepo.save(op);
        log.log(actor, "UPDATE", "operator", "operatorId=" + operatorID);
        return saved;
    }
}
