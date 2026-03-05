package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.dto.CreateIssueRequest;
import com.itsm.itsmsystem.dto.IssueRequest;
import com.itsm.itsmsystem.dto.IssueResponse;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.exception.ResourceNotFoundException;
import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.model.entity.Issue;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.AuditLogRepository;
import com.itsm.itsmsystem.repository.IssueRepository;
import com.itsm.itsmsystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class IssueService {

    private final IssueRepository issueRepository;
    private final TicketRepository ticketRepository;
    private final AuditLogRepository auditLogRepository;

    /**
     * Create new issue and auto-create ticket
     * This is the main method for faculty to raise issues
     */
    public IssueResponse createIssue(IssueRequest request, User creator) {
        log.info("Creating issue for user: {}", creator.getEmail());
        
        try {
            // 1. Create and save issue
            Issue issue = new Issue();
            issue.setTitle(request.getTitle());
            issue.setDescription(request.getDescription());
            issue.setCategory(request.getCategory());
            issue.setPriority(request.getPriority());
            issue.setLocation(request.getLocation());
            issue.setCreatedBy(creator);
            issue.setCreatedAt(LocalDateTime.now());
            
            Issue savedIssue = issueRepository.save(issue);
            log.info("Issue saved with ID: {}", savedIssue.getId());

            // 2. Auto-create linked ticket
            Ticket ticket = new Ticket();
            ticket.setIssue(savedIssue);
            ticket.setTitle(request.getTitle());
            ticket.setDescription(request.getDescription());
            ticket.setCategory(request.getCategory());
            ticket.setPriority(request.getPriority());
            ticket.setLocation(request.getLocation());
            ticket.setStatus(TicketStatus.OPEN);
            ticket.setCreatedBy(creator);
            ticket.setCreatedAt(LocalDateTime.now());
            ticket.setUpdatedAt(LocalDateTime.now());
            
            Ticket savedTicket = ticketRepository.save(ticket);
            log.info("Ticket auto-created with ID: {}", savedTicket.getId());

            // 3. Create audit log
            createAuditLog(savedTicket, creator, "ISSUE_CREATED", 
                "Issue created: " + savedIssue.getTitle() + " (Auto-created ticket #" + savedTicket.getId() + ")");

            // 4. Return success response
            return IssueResponse.builder()
                .message("Issue created successfully")
                .issueId(savedIssue.getId())
                .ticketId(savedTicket.getId())
                .status("OPEN")
                .build();
                
        } catch (Exception e) {
            log.error("Error creating issue: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create issue: " + e.getMessage());
        }
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

    /**
     * Create audit log entry
     */
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

