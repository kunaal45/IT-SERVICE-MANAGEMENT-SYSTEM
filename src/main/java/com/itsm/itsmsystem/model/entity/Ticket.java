package com.itsm.itsmsystem.model.entity;

import com.itsm.itsmsystem.enums.IssueCategory;
import com.itsm.itsmsystem.enums.TicketPriority;
import com.itsm.itsmsystem.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets", indexes = {
    @Index(name = "idx_ticket_status", columnList = "status"),
    @Index(name = "idx_ticket_priority", columnList = "priority"),
    @Index(name = "idx_ticket_created_by", columnList = "created_by_id"),
    @Index(name = "idx_ticket_assigned_to", columnList = "assigned_to_id"),
    @Index(name = "idx_ticket_issue", columnList = "issue_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the Issue that created this ticket
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issue_id", nullable = true)  // Nullable for backward compatibility with existing demo data
    private Issue issue;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private IssueCategory category;

    @Column(length = 200)
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    
    // Resolution notes - what was done to fix the issue
    @Column(columnDefinition = "TEXT")
    private String resolutionNotes;
    
    // For resource requests - quantity needed
    @Column
    private Integer quantity;
    
    // Flag to indicate if this is a resource request
    @Column(name = "resource_request")
    private Boolean resourceRequest;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = TicketStatus.OPEN;
        }
        if (resourceRequest == null) {
            resourceRequest = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}