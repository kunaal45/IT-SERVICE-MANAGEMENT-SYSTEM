package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.dto.AssignTicketRequest;
import com.itsm.itsmsystem.dto.CreateTicketRequest;
import com.itsm.itsmsystem.dto.DashboardStatsDto;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final WorkflowValidator workflowValidator;

    // ===== CREATE TICKET (FACULTY / ADMIN) =====

    public Ticket createTicket(CreateTicketRequest request, User creator) {
        workflowValidator.validateRoleForAction(creator.getRole(), "CREATE_TICKET");

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCategory(request.getCategory());
        ticket.setLocation(request.getLocation());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(creator);

        Ticket saved = ticketRepository.save(ticket);
        createAuditLog(saved, creator, "TICKET_CREATED",
                "Ticket created by " + creator.getName() + ": " + ticket.getTitle());
        return saved;
    }

    // ===== ASSIGN (ADMIN only) =====

    public Ticket assignTicket(Long ticketId, AssignTicketRequest request, User admin) {
        workflowValidator.validateRoleForAction(admin.getRole(), "ASSIGN_TICKET");

        Ticket ticket = getTicketById(ticketId);
        workflowValidator.validateTransition(ticket.getStatus(), TicketStatus.ASSIGNED);

        User engineer = getUserById(request.getEngineerId());
        if (engineer.getRole() != Role.ENGINEER) {
            throw new UnauthorizedException("Can only assign to ENGINEER role users");
        }

        ticket.setAssignedTo(engineer);
        ticket.setStatus(TicketStatus.ASSIGNED);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);
        createAuditLog(saved, admin, "TICKET_ASSIGNED",
                "Assigned to " + engineer.getName() + " by " + admin.getName());
        return saved;
    }

    // ===== START PROGRESS (ENGINEER only) =====

    public Ticket startProgress(Long ticketId, User engineer) {
        workflowValidator.validateRoleForAction(engineer.getRole(), "START_PROGRESS");

        Ticket ticket = getTicketById(ticketId);

        if (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(engineer.getId())) {
            throw new UnauthorizedException("This ticket is not assigned to you");
        }

        workflowValidator.validateTransition(ticket.getStatus(), TicketStatus.IN_PROGRESS);

        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);
        createAuditLog(saved, engineer, "TICKET_STARTED",
                "Progress started by " + engineer.getName());
        return saved;
    }

    // ===== RESOLVE (ENGINEER / ADMIN) =====

    public Ticket resolveTicket(Long ticketId, User engineer) {
        workflowValidator.validateRoleForAction(engineer.getRole(), "RESOLVE_TICKET");

        Ticket ticket = getTicketById(ticketId);

        if (engineer.getRole() == Role.ENGINEER) {
            if (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(engineer.getId())) {
                throw new UnauthorizedException("This ticket is not assigned to you");
            }
        }

        workflowValidator.validateTransition(ticket.getStatus(), TicketStatus.RESOLVED);

        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setResolvedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);
        createAuditLog(saved, engineer, "TICKET_RESOLVED",
                "Resolved by " + engineer.getName());
        return saved;
    }

    // ===== CLOSE (FACULTY / ADMIN) =====

    public Ticket closeTicket(Long ticketId, User faculty) {
        workflowValidator.validateRoleForAction(faculty.getRole(), "CLOSE_TICKET");

        Ticket ticket = getTicketById(ticketId);

        if (faculty.getRole() == Role.FACULTY) {
            if (!ticket.getCreatedBy().getId().equals(faculty.getId())) {
                throw new UnauthorizedException("You can only close your own tickets");
            }
        }

        workflowValidator.validateTransition(ticket.getStatus(), TicketStatus.CLOSED);

        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setClosedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);
        createAuditLog(saved, faculty, "TICKET_CLOSED",
                "Closed by " + faculty.getName());
        return saved;
    }

    // ===== QUERIES =====

    public List<Ticket> getTicketsByRole(User user) {
        return switch (user.getRole()) {
            case ADMIN -> ticketRepository.findAll();
            case ENGINEER, SUPPORT_ENGINEER -> ticketRepository.findByAssignedToId(user.getId());
            case FACULTY, STUDENT -> ticketRepository.findByCreatedById(user.getId());
        };
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
    }

    public Page<Ticket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    public Page<Ticket> getTicketsByAssignee(User engineer, Pageable pageable) {
        return ticketRepository.findByAssignedToId(engineer.getId(), pageable);
    }

    public Page<Ticket> getTicketsByCreator(User user, Pageable pageable) {
        return ticketRepository.findByCreatedById(user.getId(), pageable);
    }

    public List<AuditLog> getTicketAuditLog(Long ticketId) {
        return auditLogRepository.findByTicketIdOrderByCreatedAtDesc(ticketId);
    }

    public List<User> getEngineers() {
        return userRepository.findByRole(Role.ENGINEER);
    }

    /**
     * Returns dashboard stats tailored to the caller's role.
     */
    public DashboardStatsDto getDashboardStatsByRole(User user) {
        return switch (user.getRole()) {
            case ADMIN -> DashboardStatsDto.builder()
                    .totalTickets(ticketRepository.count())
                    .openTickets(ticketRepository.countByStatus(TicketStatus.OPEN))
                    .assignedTickets(ticketRepository.countByStatus(TicketStatus.ASSIGNED))
                    .inProgressTickets(ticketRepository.countByStatus(TicketStatus.IN_PROGRESS))
                    .resolvedTickets(ticketRepository.countByStatus(TicketStatus.RESOLVED))
                    .closedTickets(ticketRepository.countByStatus(TicketStatus.CLOSED))
                    .build();
            case ENGINEER, SUPPORT_ENGINEER -> DashboardStatsDto.builder()
                    .totalTickets(ticketRepository.countByAssignedToId(user.getId()))
                    .assignedTickets(ticketRepository.countByAssignedToIdAndStatus(user.getId(), TicketStatus.ASSIGNED))
                    .inProgressTickets(ticketRepository.countByAssignedToIdAndStatus(user.getId(), TicketStatus.IN_PROGRESS))
                    .resolvedTickets(ticketRepository.countByAssignedToIdAndStatus(user.getId(), TicketStatus.RESOLVED))
                    .build();
            default -> DashboardStatsDto.builder()
                    .totalTickets(ticketRepository.countByCreatedById(user.getId()))
                    .openTickets(ticketRepository.countByCreatedByIdAndStatus(user.getId(), TicketStatus.OPEN))
                    .resolvedTickets(ticketRepository.countByCreatedByIdAndStatus(user.getId(), TicketStatus.RESOLVED))
                    .closedTickets(ticketRepository.countByCreatedByIdAndStatus(user.getId(), TicketStatus.CLOSED))
                    .build();
        };
    }

    // ===== HELPERS =====

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
