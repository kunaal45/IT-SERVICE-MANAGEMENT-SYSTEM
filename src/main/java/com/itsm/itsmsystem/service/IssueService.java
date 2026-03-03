package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.dto.CreateIssueRequest;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.exception.ResourceNotFoundException;
import com.itsm.itsmsystem.exception.UnauthorizedException;
import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.model.entity.Issue;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.AuditLogRepository;
import com.itsm.itsmsystem.repository.IssueRepository;
import com.itsm.itsmsystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Issue service - handles issue creation and auto-ticket generation.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;
    private final TicketRepository ticketRepository;
    private final AuditLogRepository auditLogRepository;

    /**
     * Create new issue and auto-create ticket
     */
    public Ticket createIssue(CreateIssueRequest request, User creator) {
        // Create issue
        Issue issue = new Issue();
        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setCategory(request.getCategory());
        issue.setPriority(request.getPriority());
        issue.setLocation(request.getLocation());
        issue.setCreatedBy(creator);
        
        Issue savedIssue = issueRepository.save(issue);

        // Auto-create ticket
        Ticket ticket = new Ticket();
        ticket.setIssue(savedIssue);
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setCategory(request.getCategory());
        ticket.setPriority(request.getPriority());
        ticket.setLocation(request.getLocation());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(creator);
        
        Ticket savedTicket = ticketRepository.save(ticket);

        // Create audit log
        AuditLog log = AuditLog.builder()
            .action("ISSUE_CREATED")
            .ticket(savedTicket)
            .user(creator)
            .details("Issue created: " + savedIssue.getTitle() + " (Auto-created ticket)")
            .createdAt(LocalDateTime.now())
            .build();
        auditLogRepository.save(log);

        return savedTicket;
    }

    /**
     * Get my issues (paginated)
     */
    public Page<Issue> getMyIssues(User user, Pageable pageable) {
        return issueRepository.findByCreatedById(user.getId(), pageable);
    }

    /**
     * Get single issue
     */
    public Issue getIssue(Long issueId) {
        return issueRepository.findById(issueId)
            .orElseThrow(() -> new ResourceNotFoundException("Issue not found with id: " + issueId));
    }
}
