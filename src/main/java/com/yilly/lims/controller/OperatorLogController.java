package com.yilly.lims.controller;

import com.yilly.lims.repository.OperatorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor
@RequestMapping("/logs")
public class OperatorLogController {

    private final OperatorLogRepository logRepo;

    // 简单全部操作日志
    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(logRepo.findAll());
    }
}
