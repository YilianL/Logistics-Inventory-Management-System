package com.yilly.lims.service;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.entity.OperatorLog;
import com.yilly.lims.repository.OperatorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class LogService {
    private final OperatorLogRepository logRepo;

    public void log(Operator operator, String action, String resource, String details) {
        var l = new OperatorLog();
        l.setOperator(operator);
        l.setAction(action);
        l.setResource(resource);
        l.setDetails(details);
        logRepo.save(l);
    }
}
