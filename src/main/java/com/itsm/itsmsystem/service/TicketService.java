package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.dto.AssignTicketRequest;
import com.itsm.itsmsystem.dto.CreateTicketRequest;
import com.itsm.itsmsystem.dto.DashboardStatsDto;
import com.itsm.itsmsystem.enums.EngineerLevel;
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.enums.TicketPriority;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.exception.ResourceNotFoundException;
import com.itsm.itsmsystem.exception.UnauthorizedException;
import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.TicketRepository;
import com.itsm.itsmsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final WorkflowValidator workflowValidator;
    private final SLAService slaService;

    // ===== CREATE TICKET (FACULTY / ADMIN) =====

    public Ticket createTicket(CreateTicketRequest request, User creator) {
        workflowValidator.validateRoleForAction(creator.getRole(), "CREATE_TICKET");

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCategory(request.getCategory());
        ticket.setLocation(request.getLocation());
        ticket.setCreatedBy(creator);
        ticket.setSlaBreached(false);

        // Automatic Assignment Logic (Junior Engineers of the Category)
        List<User> jrEngineers = userRepository.findEngineerByLevelAndCategory(EngineerLevel.JUNIOR,
                request.getCategory());
        if (!jrEngineers.isEmpty()) {
            User eng = jrEngineers.get(0); // For now, assign to the first one found
            ticket.setAssignedTo(eng);
            ticket.setStatus(TicketStatus.ASSIGNED);
        }

        // SLA Deadline calculation from dynamic rules
        int slaHours = slaService.getHoursForPriority(request.getPriority().name());
        ticket.setSlaDeadline(LocalDateTime.now().plusHours(slaHours));

        Ticket saved = ticketRepository.save(ticket);

        String action = saved.getAssignedTo() != null ? "TICKET_AUTO_ASSIGNED" : "TICKET_CREATED";
        String details = saved.getAssignedTo() != null
                ? "Ticket created by " + creator.getName() + " and auto-assigned to Junior Engineer "
                        + saved.getAssignedTo().getName()
                : "Ticket created by " + creator.getName() + ": " + ticket.getTitle();

        createAuditLog(saved, creator, action, details);
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

        String action = ticket.getAssignedTo() != null ? "TICKET_REASSIGNED" : "TICKET_ASSIGNED";
        String details = ticket.getAssignedTo() != null
                ? "Reassigned from " + ticket.getAssignedTo().getName() + " to " + engineer.getName() + " by "
                        + admin.getName()
                : "Assigned to " + engineer.getName() + " by " + admin.getName();

        ticket.setAssignedTo(engineer);
        ticket.setStatus(TicketStatus.ASSIGNED);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);
        createAuditLog(saved, admin, action, details);
        return saved;
    }

    public Ticket reassignTicket(Long ticketId, Long newEngineerId, User agent) {
        workflowValidator.validateRoleForAction(agent.getRole(), "REASSIGN_TICKET");

        Ticket ticket = getTicketById(ticketId);
        User newEngineer = getUserById(newEngineerId);
        if (newEngineer.getRole() != Role.ENGINEER) {
            throw new UnauthorizedException("Can only reassign to ENGINEER role users");
        }

        String oldEngineerName = ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : "None";
        String details = "Reassigned from " + oldEngineerName + " to " + newEngineer.getName() + " by "
                + agent.getName();

        ticket.setAssignedTo(newEngineer);
        ticket.setStatus(TicketStatus.ASSIGNED);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);
        createAuditLog(saved, agent, "TICKET_REASSIGNED", details);
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

    @Scheduled(fixedRate = 60000)
    public void markSlaBreaches() {
        List<Ticket> activeTickets = ticketRepository.findAll().stream()
                .filter(t -> t.getStatus() != TicketStatus.RESOLVED && t.getStatus() != TicketStatus.CLOSED)
                .filter(t -> t.getSlaDeadline() != null && t.getSlaBreached() != null && !t.getSlaBreached())
                .filter(t -> LocalDateTime.now().isAfter(t.getSlaDeadline()))
                .toList();

        if (activeTickets.isEmpty())
            return;

        User systemAdmin = userRepository.findByRole(Role.ADMIN).stream().findFirst().orElse(null);
        if (systemAdmin == null)
            return;

        for (Ticket ticket : activeTickets) {
            ticket.setSlaBreached(true);

            // Escalate to Senior Engineer of the same Category
            List<User> srEngineers = userRepository.findEngineerByLevelAndCategory(EngineerLevel.SENIOR,
                    ticket.getCategory());
            if (!srEngineers.isEmpty()) {
                User srEng = srEngineers.get(0);
                ticket.setAssignedTo(srEng);
                ticket.setStatus(TicketStatus.ASSIGNED); // Reset to assigned for the senior
                createAuditLog(ticket, systemAdmin, "TICKET_REASSIGNED",
                        "Escalated to Senior Engineer " + srEng.getName() + " due to SLA breach.");
            }

            ticketRepository.save(ticket);
            createAuditLog(ticket, systemAdmin, "SLA_BREACHED", "SLA deadline exceeded. Ticket marked as Breached.");
        }
    }

    public Ticket escalatePriority(Long ticketId, TicketPriority priority, User agent) {
        workflowValidator.validateRoleForAction(agent.getRole(), "ESCALATE_PRIORITY");

        Ticket ticket = getTicketById(ticketId);
        TicketPriority oldPriority = ticket.getPriority();
        ticket.setPriority(priority);

        // Recalculate SLA based on new priority from dynamic rules
        int hours = slaService.getHoursForPriority(priority.name());
        ticket.setSlaDeadline(ticket.getCreatedAt().plusHours(hours));
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);
        createAuditLog(saved, agent, "PRIORITY_ESCALATED",
                "Priority escalated from " + oldPriority + " to " + priority + " by " + agent.getName(),
                oldPriority.name(), priority.name());
        return saved;
    }

    // ===== RESOLVE (ENGINEER / ADMIN) =====

    public Ticket resolveTicket(Long ticketId, User engineer, String resolutionNotes) {
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
        if (resolutionNotes != null && !resolutionNotes.isBlank()) {
            ticket.setResolutionNotes(resolutionNotes);
        }

        Ticket saved = ticketRepository.save(ticket);
        String details = "Resolved by " + engineer.getName();
        if (resolutionNotes != null && !resolutionNotes.isBlank()) {
            details += ". Notes: " + resolutionNotes;
        }
        createAuditLog(saved, engineer, "TICKET_RESOLVED", details);
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
            case ADMIN, SERVICE_DESK -> ticketRepository.findAll();
            case ENGINEER -> ticketRepository.findByAssignedToId(user.getId());
            case FACULTY -> ticketRepository.findByCreatedById(user.getId());
        };
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
    }

    public Page<Ticket> getAllTickets(@NonNull Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    public Page<Ticket> getTicketsByAssignee(User engineer, @NonNull Pageable pageable) {
        return ticketRepository.findByAssignedToId(engineer.getId(), pageable);
    }

    public Page<Ticket> getTicketsByCreator(User user, Pageable pageable) {
        return ticketRepository.findByCreatedById(user.getId(), pageable);
    }

    public List<AuditLog> getTicketAuditLog(Long ticketId) {
        return auditService.getAuditLogsByTicketId(ticketId);
    }

    public List<User> getEngineers() {
        return userRepository.findByRole(Role.ENGINEER);
    }

    /**
     * Returns dashboard stats tailored to the caller's role.
     */
    public DashboardStatsDto getDashboardStatsByRole(User user) {
        return switch (user.getRole()) {
            case ADMIN, SERVICE_DESK -> DashboardStatsDto.builder()
                    .totalTickets(ticketRepository.count())
                    .openTickets(ticketRepository.countByStatus(TicketStatus.OPEN))
                    .assignedTickets(ticketRepository.countByStatus(TicketStatus.ASSIGNED))
                    .inProgressTickets(ticketRepository.countByStatus(TicketStatus.IN_PROGRESS))
                    .resolvedTickets(ticketRepository.countByStatus(TicketStatus.RESOLVED))
                    .closedTickets(ticketRepository.countByStatus(TicketStatus.CLOSED))
                    .build();
            case ENGINEER -> DashboardStatsDto.builder()
                    .totalTickets(ticketRepository.countByAssignedToId(user.getId()))
                    .assignedTickets(ticketRepository.countByAssignedToIdAndStatus(user.getId(), TicketStatus.ASSIGNED))
                    .inProgressTickets(
                            ticketRepository.countByAssignedToIdAndStatus(user.getId(), TicketStatus.IN_PROGRESS))
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
        createAuditLog(ticket, user, action, details, null, null);
    }

    private void createAuditLog(Ticket ticket, User user, String action, String details, String prev, String next) {
        auditService.logAction(action, user, ticket.getId(), details, prev, next);
    }
}
