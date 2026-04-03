package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final com.itsm.itsmsystem.repository.TicketRepository ticketRepository;

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }

    public List<AuditLog> getAuditLogsByTicketId(Long ticketId) {
        return auditLogRepository.findByTicketIdOrderByTimestampDesc(ticketId);
    }

    public List<AuditLog> getRecentAuditLogs() {
        return auditLogRepository.findTop10ByOrderByTimestampDesc();
    }

    @SuppressWarnings("null")
    public AuditLog logAction(String actionType, User user, Long ticketId, String details, String previousValue, String newValue) {
        Ticket ticket = null;
        if (ticketId != null) {
            ticket = ticketRepository.findById(ticketId).orElse(null);
        }

        AuditLog log = AuditLog.builder()
                .actionType(actionType)
                .performedBy(user != null ? user.getName() : "SYSTEM")
                .ticketId(ticketId)
                .user(user)
                .userRole(user != null ? user.getRole().name() : "SYSTEM")
                .ticket(ticket)
                .details(details)
                .previousValue(previousValue)
                .newValue(newValue)
                .timestamp(LocalDateTime.now())
                .build();

        return auditLogRepository.save(log);
    }
}
