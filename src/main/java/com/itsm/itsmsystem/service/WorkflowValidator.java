package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.exception.InvalidTicketStateException;
import com.itsm.itsmsystem.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Strict workflow validator using state machine pattern.
 * Enforces valid status transitions and role-based permissions.
 * No transitions allowed outside of defined rules.
 */
@Component
public class WorkflowValidator {

    // Define all valid status transitions
    private static final Map<TicketStatus, Set<TicketStatus>> VALID_TRANSITIONS = new EnumMap<>(TicketStatus.class);

    static {
        // From OPEN: can only go to ASSIGNED
        VALID_TRANSITIONS.put(TicketStatus.OPEN, EnumSet.of(TicketStatus.ASSIGNED));

        // From ASSIGNED: can go to IN_PROGRESS
        VALID_TRANSITIONS.put(TicketStatus.ASSIGNED, EnumSet.of(TicketStatus.IN_PROGRESS));

        // From IN_PROGRESS: can only go to RESOLVED
        VALID_TRANSITIONS.put(TicketStatus.IN_PROGRESS, EnumSet.of(TicketStatus.RESOLVED));

        // From RESOLVED: can only go to CLOSED
        VALID_TRANSITIONS.put(TicketStatus.RESOLVED, EnumSet.of(TicketStatus.CLOSED));

        // From CLOSED: cannot transition anywhere
        VALID_TRANSITIONS.put(TicketStatus.CLOSED, EnumSet.noneOf(TicketStatus.class));
    }

    /**
     * Validate if a status transition is allowed
     */
    public void validateTransition(TicketStatus currentStatus, TicketStatus newStatus) {
        if (currentStatus == newStatus) {
            return; // No transition needed
        }

        Set<TicketStatus> allowedTransitions = VALID_TRANSITIONS.get(currentStatus);
        if (allowedTransitions == null || !allowedTransitions.contains(newStatus)) {
            throw new InvalidTicketStateException(
                    String.format("Cannot transition ticket from %s to %s", currentStatus, newStatus));
        }
    }

    /**
     * Validate if user has permission to perform an action
     */
    public void validateRoleForAction(Role userRole, String action) {
        switch (action) {
            case "CREATE_TICKET" -> {
                if (userRole != Role.FACULTY && userRole != Role.ADMIN) {
                    throw new UnauthorizedException("Only FACULTY or ADMIN can create tickets");
                }
            }
            case "ASSIGN_TICKET", "REASSIGN_TICKET" -> {
                if (userRole != Role.ADMIN && userRole != Role.SERVICE_DESK) {
                    throw new UnauthorizedException("Only ADMIN or SERVICE_DESK can assign/reassign tickets");
                }
            }
            case "START_PROGRESS" -> {
                if (userRole != Role.ENGINEER) {
                    throw new UnauthorizedException("Only ENGINEER can start ticket progress");
                }
            }
            case "RESOLVE_TICKET" -> {
                if (userRole != Role.ENGINEER && userRole != Role.ADMIN && userRole != Role.SERVICE_DESK) {
                    throw new UnauthorizedException("Only ENGINEER, ADMIN or SERVICE_DESK can resolve tickets");
                }
            }
            case "CLOSE_TICKET" -> {
                if (userRole != Role.FACULTY && userRole != Role.ADMIN) {
                    throw new UnauthorizedException("Only FACULTY or ADMIN can close tickets");
                }
            }
            case "VIEW_ALL_TICKETS" -> {
                if (userRole != Role.ADMIN && userRole != Role.SERVICE_DESK) {
                    throw new UnauthorizedException("Only ADMIN or SERVICE_DESK can view all tickets");
                }
            }
            case "ESCALATE_PRIORITY" -> {
                if (userRole != Role.ADMIN && userRole != Role.SERVICE_DESK) {
                    throw new UnauthorizedException("Only ADMIN or SERVICE_DESK can escalate priority");
                }
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        }
    }
}
