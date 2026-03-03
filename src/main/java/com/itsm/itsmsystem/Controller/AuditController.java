package com.itsm.itsmsystem.Controller;

import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuditController {
    
    private final AuditService auditService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(auditService.getAllAuditLogs());
    }
    
    @GetMapping("/ticket/{ticketId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLog>> getTicketAuditLogs(@PathVariable Long ticketId) {
        return ResponseEntity.ok(auditService.getAuditLogsByTicketId(ticketId));
    }
}
