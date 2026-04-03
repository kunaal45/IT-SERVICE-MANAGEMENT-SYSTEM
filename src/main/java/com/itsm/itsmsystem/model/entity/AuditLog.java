package com.itsm.itsmsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_audit_logs", indexes = {
        @Index(name = "idx_audit_ticket", columnList = "ticket_id"),
        @Index(name = "idx_audit_user", columnList = "actor_id"),
        @Index(name = "idx_audit_action_type", columnList = "action_type"),
        @Index(name = "idx_audit_timestamp", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, name = "action_type")
    private String actionType;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "user_role", length = 20)
    private String userRole;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime timestamp;

    @Column(name = "performed_by", length = 100)
    private String performedBy;

    @Column(name = "ticket_id_ref")
    private Long ticketId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actor_id", nullable = true)
    private User user;

    @Column(name = "previous_value", columnDefinition = "TEXT")
    private String previousValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
