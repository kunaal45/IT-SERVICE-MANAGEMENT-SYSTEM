package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.dto.DashboardStatsDto;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Aggregated dashboard statistics service.
 * Provides role-based dashboard data from database.
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TicketRepository ticketRepository;

    /**
     * Get admin dashboard statistics
     */
    public DashboardStatsDto getAdminDashboard() {
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
     * Get engineer dashboard statistics
     */
    public DashboardStatsDto getEngineerDashboard(User engineer) {
        long assignedToMe = ticketRepository.countByAssignedToId(engineer.getId());
        long inProgressByMe = ticketRepository.countByAssignedToIdAndStatus(engineer.getId(), TicketStatus.IN_PROGRESS);
        long resolvedByMe = ticketRepository.countByAssignedToIdAndStatus(engineer.getId(), TicketStatus.RESOLVED);

        return DashboardStatsDto.builder()
            .assignedTickets(assignedToMe)
            .inProgressTickets(inProgressByMe)
            .resolvedTickets(resolvedByMe)
            .build();
    }

    /**
     * Get faculty dashboard statistics
     */
    public DashboardStatsDto getFacultyDashboard(User faculty) {
        long createdByMe = ticketRepository.countByCreatedById(faculty.getId());
        long openByMe = ticketRepository.countByCreatedByIdAndStatus(faculty.getId(), TicketStatus.OPEN);
        long resolvedByMe = ticketRepository.countByCreatedByIdAndStatus(faculty.getId(), TicketStatus.RESOLVED);
        long closedByMe = ticketRepository.countByCreatedByIdAndStatus(faculty.getId(), TicketStatus.CLOSED);

        return DashboardStatsDto.builder()
            .totalTickets(createdByMe)
            .openTickets(openByMe)
            .resolvedTickets(resolvedByMe)
            .closedTickets(closedByMe)
            .build();
    }
}
