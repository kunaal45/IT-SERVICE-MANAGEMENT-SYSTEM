package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.exception.ResourceNotFoundException;
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.exception.UnauthorizedException;
import com.itsm.itsmsystem.model.entity.Comment;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.CommentRepository;
import com.itsm.itsmsystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final AuditService auditService;

    public Comment addComment(Long ticketId, String content, User user) {
        if (user.getRole() != Role.FACULTY) {
            throw new UnauthorizedException("Only Faculty users can add comments to tickets.");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setUser(user);
        comment.setContent(content);

        Comment saved = commentRepository.save(comment);

        // Audit Log
        auditService.logAction(
                "COMMENT_ADDED",
                user,
                ticketId,
                "Faculty member " + user.getName() + " added a comment to ticket #" + ticketId,
                null,
                null
        );

        return saved;
    }

    public List<Comment> getCommentsByTicketId(Long ticketId) {
        return commentRepository.findByTicketIdOrderByCreatedAtDesc(ticketId);
    }
}
