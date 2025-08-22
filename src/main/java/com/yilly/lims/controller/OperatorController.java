package com.yilly.lims.controller;

import com.yilly.lims.service.OperatorService;
import com.yilly.lims.entity.Operator;
import com.yilly.lims.repository.OperatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequiredArgsConstructor
@RequestMapping("/operator")
public class OperatorController {

    private final OperatorService operatorService;
    private final OperatorRepository operatorRepo;

    // 用户注册（由admin或系统管理员操作）
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String actor, @RequestBody Map<String, String> body) {
        //定位操作员
        Operator doer = operatorRepo.findByUsername(actor).orElse(null);
        //完成注册
        var saved = operatorService.register(doer, body.get("username"), body.get("password"), body.get("roleName"));
        //返回注册信息
        return ResponseEntity.ok(Map.of("operatorID", saved.getOperatorID(), "message", "Register successful"));
    }

    // 获取当前用户信息
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestParam String actor) {
        //定位
        var me = operatorRepo.findByUsername(actor).orElse(null);
        //无权限
        if (me == null)
            return ResponseEntity.status(401).body(Map.of("message","Unauthorized"));
        return ResponseEntity.ok(operatorService.profile(me));
    }

    // 用户信息更新（改密/换角色）
    @PutMapping("/{operatorID}")
    public ResponseEntity<?> update(@RequestParam String actor, @PathVariable Long operatorID, @RequestBody Map<String, String> body) {
        //定位
        Operator doer = operatorRepo.findByUsername(actor).orElse(null);
        //更新信息
        var saved = operatorService.update(doer, operatorID, body.get("password"), body.get("roleName"));
        return ResponseEntity.ok(Map.of("operatorID", saved.getOperatorID(), "message", "Update successful"));
    }
}
