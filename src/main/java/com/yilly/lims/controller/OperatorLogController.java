package com.yilly.lims.controller;

import com.yilly.lims.repository.OperatorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor
@RequestMapping("/logs")
public class OperatorLogController {

    private final OperatorLogRepository logRepo;

    // 简单全量查询（可扩展为按操作者/时间/动作查询）
    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(logRepo.findAll());
    }
}
