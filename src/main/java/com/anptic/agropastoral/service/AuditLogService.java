package com.anptic.agropastoral.service;

import com.anptic.agropastoral.model.AuditLog;

public interface AuditLogService {
    void log(AuditLog auditLog);
}
