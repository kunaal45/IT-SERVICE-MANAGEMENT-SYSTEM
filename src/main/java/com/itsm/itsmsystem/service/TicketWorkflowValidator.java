package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.exception.InvalidStatusTransitionException;
import com.itsm.itsmsystem.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Strict workflow validator using state machine pattern
 * Ensures only valid status transitions are allowed
 */
@Component
public class TicketWorkflowValidator {

    // Define valid status transitions
    private static final Map<TicketStatus, Set<TicketStatus>> VALID_TRANSITIONS = new EnumMap<>(TicketStatus.class);

    static {
        VALID_TRANSITIONS.put(TicketStatus.OPEN, EnumSet.of(TicketStatus.ASSIGNED));
        VALID_TRANSITIONS.put(TicketStatus.ASSIGNED, EnumSet.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED));
        VALID_TRANSITIONS.put(TicketStatus.IN_PROGRESS, EnumSet.of(TicketStatus.RESOLVED));
        VALID_TRANSITIONS.put(TicketStatus.RESOLVED, EnumSet.of(TicketStatus.CLOSED));
        VALID_TRANSITIONS.put(TicketStatus.CLOSED, EnumSet.noneOf(TicketStatus.class)); // Cannot transition from CLOSED
    }

    /**
     * Validate if status transition is allowed
     */
    public void validateTransition(TicketStatus currentStatus, TicketStatus newStatus) {
        if (currentStatus == newStatus) {
            return; // No transition
        }

        Set<TicketStatus> allowedTransitions = VALID_TRANSITIONS.get(currentStatus);
        if (allowedTransitions == null || !allowedTransitions.contains(newStatus)) {
            throw new InvalidStatusTransitionException(
                String.format("Cannot transition from %s to %s", currentStatus, newStatus)
            );
        }
    }

    /**
     * Validate if user role can perform this action
     */
    public void validateRoleForAction(Role userRole, String action) {
        switch (action) {
            case "CREATE" -> {
                if (userRole != Role.FACULTY && userRole != Role.ADMIN) {
                    throw new UnauthorizedException("Only FACULTY or ADMIN can create tickets");
                }
            }
            case "ASSIGN" -> {
                if (userRole != Role.ADMIN) {
                    throw new UnauthorizedException("Only ADMIN can assign tickets");
                }
            }
            case "START_PROGRESS" -> {
                if (userRole != Role.ENGINEER) {
                    throw new UnauthorizedException("Only ENGINEER can start progress");
                }
            }
            case "RESOLVE" -> {
                if (userRole != Role.ENGINEER && userRole != Role.ADMIN) {
                    throw new UnauthorizedException("Only ENGINEER or ADMIN can resolve tickets");
                }
            }
            case "CLOSE" -> {
                if (userRole != Role.FACULTY) {
                    throw new UnauthorizedException("Only FACULTY can close tickets");
                }
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    /**
     * Get allowed next statuses for current status
     */
    public Set<TicketStatus> getAllowedNextStatuses(TicketStatus currentStatus) {
        return VALID_TRANSITIONS.getOrDefault(currentStatus, EnumSet.noneOf(TicketStatus.class));
    }
}
