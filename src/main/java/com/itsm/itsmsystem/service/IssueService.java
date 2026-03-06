package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.dto.CreateIssueRequest;
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
}

