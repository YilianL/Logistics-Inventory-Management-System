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

    //查找用户
    public Optional<Operator> byUsername(String username) {
        return operatorRepo.findByUsername(username);
    }

    //注册新用户
    @Transactional
    public Operator register(Operator actor, String username, String rawPassword, String roleName) {
        //查找同名用户
        operatorRepo.findByUsername(username).
                ifPresent(o -> { throw new IllegalStateException("用户名已存在"); });
        //新建用户信息
        var op = new Operator();
        op.setUsername(username);
        op.setPassword(encoder.encode(rawPassword));
        //绑定权限
        if (roleName != null && !roleName.isBlank()) {
            op.setRole(roleRepo.findByRoleName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("角色不存在")));
        }
        //存储用户
        var saved = operatorRepo.save(op);
        //记录操作信息
        log.log(actor, "CREATE", "operator", "username=" + username);
        //返回用户信息
        return saved;
    }

    //调出信息
    public Map<String, Object> profile(Operator me) {

        String roleName = "USER";
        List<String> perms = List.of();

        //调出角色信息
        if (me.getRole() != null) {
            roleName = me.getRole().getRoleName();
            //调出权限信息
            List<String> p = new ArrayList<>();
            if (me.getRole().getPermissions() != null) {
                for (Permission perm : me.getRole().getPermissions()) {
                    p.add(perm.getPermissionName());
                }
            }
            perms = p;
        }

        return Map.of("operatorID", me.getOperatorID(), "username", me.getUsername(), "role", roleName, "permissions", perms);
    }

    //更新操作员信息
    @Transactional
    public Operator update(Operator actor, Long operatorID, String newPassword, String newRoleName) {
        //定位
        var op = operatorRepo.findById(operatorID)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        //更新密码
        if (newPassword != null && !newPassword.isBlank())
            op.setPassword(encoder.encode(newPassword));
        //更新权限
        if (newRoleName != null && !newRoleName.isBlank())
            op.setRole(roleRepo.findByRoleName(newRoleName)
                    .orElseThrow(() -> new IllegalArgumentException("角色不存在")));
        //存储新信息
        var saved = operatorRepo.save(op);
        //记录操作日志
        log.log(actor, "UPDATE", "operator", "operatorId=" + operatorID);
        return saved;
    }
}
