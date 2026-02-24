package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.model.AuditLog;
import com.anptic.agropastoral.repository.AuditLogRepository;
import com.anptic.agropastoral.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }
}
