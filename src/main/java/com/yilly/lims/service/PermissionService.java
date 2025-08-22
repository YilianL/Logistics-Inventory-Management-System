package com.yilly.lims.service;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.entity.Permission;
import com.yilly.lims.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepo;
    private final LogService log;

    @Transactional
    public Permission createPermission(Operator actor, String permissionName, String description, String resource, String action) {
        if (permissionName == null || permissionName.isBlank())
            throw new IllegalArgumentException("permissionName不能为空");
        //检查同名权限
        permissionRepo.findByPermissionName(permissionName)
                .ifPresent(p -> { throw new IllegalStateException("权限名重复"); });

        //设置权限信息
        var p = new Permission();
        p.setPermissionName(permissionName);
        p.setDescription(description);
        p.setResource(resource);
        p.setAction(action);

        var saved = permissionRepo.save(p);
        //记录操作日志
        log.log(actor, "CREATE", "permission", "perm=" + permissionName);
        return saved;
    }
}
