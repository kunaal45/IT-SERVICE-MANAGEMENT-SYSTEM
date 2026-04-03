package com.itsm.itsmsystem.repository;

import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.model.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByAssignedToId(Long userId);

    // Active tickets for engineer dashboard (not RESOLVED or CLOSED)
    List<Ticket> findByAssignedToIdAndStatusNotIn(Long userId, List<TicketStatus> excludedStatuses);

    List<Ticket> findByCreatedById(Long userId);

    Page<Ticket> findByAssignedToId(Long userId, Pageable pageable);

    Page<Ticket> findByCreatedById(Long userId, Pageable pageable);

    long countByStatus(TicketStatus status);

    long countByAssignedToId(Long userId);

    long countByCreatedById(Long userId);

    long countByAssignedToIdAndStatus(Long userId, TicketStatus status);

    long countByCreatedByIdAndStatus(Long userId, TicketStatus status);
}
