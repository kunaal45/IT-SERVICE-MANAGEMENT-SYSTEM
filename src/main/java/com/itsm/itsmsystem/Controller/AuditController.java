package com.itsm.itsmsystem.Controller;

import com.itsm.itsmsystem.dto.ApiResponse;
import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAllAuditLogs() {
        return ResponseEntity.ok(new ApiResponse<>(true, "All audit logs", auditService.getAllAuditLogs()));
    }

    @GetMapping("/ticket/{ticketId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getTicketAuditLogs(@PathVariable Long ticketId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket audit logs",
                auditService.getAuditLogsByTicketId(ticketId)));
    }
}
