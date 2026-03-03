package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.dto.AssignTicketRequest;
import com.itsm.itsmsystem.dto.CreateTicketRequest;
import com.itsm.itsmsystem.dto.DashboardStatsDto;
import com.itsm.itsmsystem.dto.TicketResponse;
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.exception.ResourceNotFoundException;
import com.itsm.itsmsystem.exception.UnauthorizedException;
import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.AuditLogRepository;
import com.itsm.itsmsystem.repository.TicketRepository;
import com.itsm.itsmsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core business logic for ticket management.
 * ALL workflow logic must be in this service.
 * Controllers only call this service.
 * No logic in frontend.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final WorkflowValidator workflowValidator;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ===== CREATE TICKET (FACULTY only) =====

    /**
     * Create new ticket
     * Only FACULTY and ADMIN can create
     * Ticket starts in OPEN status
     */
    public Ticket createTicket(CreateTicketRequest request, User creator) {
        // Validate role
        workflowValidator.validateRoleForAction(creator.getRole(), "CREATE_TICKET");

        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCategory(request.getCategory());
        ticket.setLocation(request.getLocation());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(creator);
        
        Ticket saved = ticketRepository.save(ticket);
        
        // Log action
        createAuditLog(saved, creator, "TICKET_CREATED", 
            "Ticket created by " + creator.getName() + ": " + ticket.getTitle());
        
        return saved;
    }

    // ===== ASSIGN TICKET (ADMIN only) =====

    /**
     * Assign ticket to engineer
     * Status transition: OPEN → ASSIGNED
     * Only ADMIN can assign
     */
    public Ticket assignTicket(Long ticketId, AssignTicketRequest request, User admin) {
        // Validate role
        workflowValidator.validateRoleForAction(admin.getRole(), "ASSIGN_TICKET");

        // Get ticket
        Ticket ticket = getTicketById(ticketId);

        // Validate current status
        workflowValidator.validateTransition(ticket.getStatus(), TicketStatus.ASSIGNED);

        // Get engineer
        User engineer = getUserById(request.getEngineerId());
        if (engineer.getRole() != Role.ENGINEER) {
            throw new UnauthorizedException("Can only assign to ENGINEER role users");
        }

        // Update ticket
        ticket.setAssignedTo(engineer);
        ticket.setStatus(TicketStatus.ASSIGNED);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);

        // Log action
        createAuditLog(saved, admin, "TICKET_ASSIGNED", 
            "Assigned to " + engineer.getName() + " by " + admin.getName());

        return saved;
    }

    // ===== START PROGRESS (ENGINEER only) =====

    /**
     * Start working on ticket
     * Status transition: ASSIGNED → IN_PROGRESS
     * Only assigned engineer can start
     */
    public Ticket startProgress(Long ticketId, User engineer) {
        // Validate role
        workflowValidator.validateRoleForAction(engineer.getRole(), "START_PROGRESS");

        // Get ticket
        Ticket ticket = getTicketById(ticketId);

        // Validate assignment
        if (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(engineer.getId())) {
            throw new UnauthorizedException("This ticket is not assigned to you");
        }

        // Validate status transition
        workflowValidator.validateTransition(ticket.getStatus(), TicketStatus.IN_PROGRESS);

        // Update ticket
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);

        // Log action
        createAuditLog(saved, engineer, "TICKET_STARTED", 
            "Progress started by " + engineer.getName());

        return saved;
    }

    // ===== RESOLVE TICKET (ENGINEER only) =====

    /**
     * Resolve/complete ticket
     * Status transition: IN_PROGRESS → RESOLVED
     * Only assigned engineer can resolve
     */
    public Ticket resolveTicket(Long ticketId, User engineer) {
        // Validate role
        workflowValidator.validateRoleForAction(engineer.getRole(), "RESOLVE_TICKET");

        // Get ticket
        Ticket ticket = getTicketById(ticketId);

        // Validate assignment (if ENGINEER)
        if (engineer.getRole() == Role.ENGINEER) {
            if (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(engineer.getId())) {
                throw new UnauthorizedException("This ticket is not assigned to you");
            }
        }

        // Validate status transition
        workflowValidator.validateTransition(ticket.getStatus(), TicketStatus.RESOLVED);

        // Update ticket
        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setResolvedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);

        // Log action
        createAuditLog(saved, engineer, "TICKET_RESOLVED", 
            "Resolved by " + engineer.getName());

        return saved;
    }

    // ===== CLOSE TICKET (FACULTY only) =====

    /**
     * Close/confirm completed ticket
     * Status transition: RESOLVED → CLOSED
     * Only ticket creator (FACULTY) can close
     */
    public Ticket closeTicket(Long ticketId, User faculty) {
        // Validate role
        workflowValidator.validateRoleForAction(faculty.getRole(), "CLOSE_TICKET");

        // Get ticket
        Ticket ticket = getTicketById(ticketId);

        // Validate creator (if FACULTY)
        if (faculty.getRole() == Role.FACULTY) {
            if (!ticket.getCreatedBy().getId().equals(faculty.getId())) {
                throw new UnauthorizedException("You can only close your own tickets");
            }
        }

        // Validate status transition (must be RESOLVED)
        workflowValidator.validateTransition(ticket.getStatus(), TicketStatus.CLOSED);

        // Update ticket
        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setClosedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);

        // Log action
        createAuditLog(saved, faculty, "TICKET_CLOSED", 
            "Closed by " + faculty.getName());

        return saved;
    }

    // ===== VIEW OPERATIONS =====

    /**
     * Get tickets filtered by user role
     * ADMIN: All tickets
     * ENGINEER: Assigned tickets
     * FACULTY: Created tickets
     */
    public List<Ticket> getTicketsByRole(User user) {
        return switch (user.getRole()) {
            case ADMIN -> ticketRepository.findAll();
            case ENGINEER, SUPPORT_ENGINEER -> ticketRepository.findByAssignedToId(user.getId());
            case FACULTY, STUDENT -> ticketRepository.findByCreatedById(user.getId());
        };
    }

    /**
     * Get single ticket
     */
    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
    }

    /**
     * Get all tickets (ADMIN only)
     */
    public List<Ticket> getAllTickets(User user) {
        workflowValidator.validateRoleForAction(user.getRole(), "VIEW_ALL_TICKETS");
        return ticketRepository.findAll();
    }

    /**
     * Get all tickets with pagination
     */
    public Page<Ticket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    /**
     * Get tickets assigned to me with pagination
     */
    public Page<Ticket> getTicketsByAssignee(User engineer, Pageable pageable) {
        return ticketRepository.findByAssignedToId(engineer.getId(), pageable);
    }

    /**
     * Get tickets created by me with pagination
     */
    public Page<Ticket> getTicketsByCreator(User user, Pageable pageable) {
        return ticketRepository.findByCreatedById(user.getId(), pageable);
    }

    /**
     * Get dashboard statistics
     */
    public DashboardStatsDto getDashboardStats() {
        return DashboardStatsDto.builder()
            .totalTickets(ticketRepository.count())
            .openTickets(ticketRepository.countByStatus(TicketStatus.OPEN))
            .assignedTickets(ticketRepository.countByStatus(TicketStatus.ASSIGNED))
            .inProgressTickets(ticketRepository.countByStatus(TicketStatus.IN_PROGRESS))
            .resolvedTickets(ticketRepository.countByStatus(TicketStatus.RESOLVED))
            .closedTickets(ticketRepository.countByStatus(TicketStatus.CLOSED))
            .build();
    }

    /**
     * Get audit log for a ticket
     */
    public List<AuditLog> getTicketAuditLog(Long ticketId) {
        return auditLogRepository.findByTicketIdOrderByCreatedAtDesc(ticketId);
    }

    // ===== HELPER METHODS =====

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private void createAuditLog(Ticket ticket, User user, String action, String details) {
        AuditLog log = AuditLog.builder()
            .ticket(ticket)
            .user(user)
            .action(action)
            .details(details)
            .createdAt(LocalDateTime.now())
            .build();
        auditLogRepository.save(log);
    }
}