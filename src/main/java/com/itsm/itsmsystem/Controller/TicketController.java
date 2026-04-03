package com.itsm.itsmsystem.Controller;

import com.itsm.itsmsystem.dto.ApiResponse;
import com.itsm.itsmsystem.dto.AssignTicketRequest;
import com.itsm.itsmsystem.dto.CreateTicketRequest;
import com.itsm.itsmsystem.dto.UpdateResolutionNotesRequest;
import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.model.entity.Comment;
import com.itsm.itsmsystem.enums.TicketPriority;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.security.JwtUtil;
import com.itsm.itsmsystem.service.CommentService;
import com.itsm.itsmsystem.service.TicketService;
import com.itsm.itsmsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CommentService commentService;

    // ===== CREATE =====

    @PostMapping
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<ApiResponse<Ticket>> createTicket(
            @Valid @RequestBody CreateTicketRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmailFromToken(authHeader);
        User creator = userService.getUserByEmail(email);
        Ticket ticket = ticketService.createTicket(request, creator);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Ticket created successfully", ticket));
    }

    // ===== LISTING =====

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<Ticket>>> getAllTickets(@NonNull Pageable pageable) {
        Page<Ticket> tickets = ticketService.getAllTickets(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "All tickets retrieved", tickets));
    }

    /**
     * /my endpoint - returns tickets based on caller's role:
     * ENGINEER → assigned tickets
     * FACULTY → created tickets
     * ADMIN → all tickets
     */
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<Ticket>>> getMyTickets(
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmailFromToken(authHeader);
        User user = userService.getUserByEmail(email);
        List<Ticket> tickets = ticketService.getTicketsByRole(user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Your tickets retrieved", tickets));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Ticket>> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket retrieved", ticket));
    }

    // ===== WORKFLOW =====

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_DESK')")
    public ResponseEntity<ApiResponse<Ticket>> assignTicket(
            @PathVariable Long id,
            @Valid @RequestBody AssignTicketRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmailFromToken(authHeader);
        User user = userService.getUserByEmail(email);
        Ticket ticket = ticketService.assignTicket(id, request, user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket assigned successfully", ticket));
    }

    @PutMapping("/{id}/reassign")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_DESK')")
    public ResponseEntity<ApiResponse<Ticket>> reassignTicket(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body,
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmailFromToken(authHeader);
        User agent = userService.getUserByEmail(email);
        Long newEngineerId = body.get("engineerId");
        Ticket ticket = ticketService.reassignTicket(id, newEngineerId, agent);

        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket reassigned successfully", ticket));
    }

    @PutMapping("/{id}/escalate")
    @PreAuthorize("hasRole('SERVICE_DESK')")
    public ResponseEntity<ApiResponse<Ticket>> escalatePriority(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmailFromToken(authHeader);
        User agent = userService.getUserByEmail(email);
        TicketPriority priority = TicketPriority.valueOf(body.get("priority"));
        Ticket ticket = ticketService.escalatePriority(id, priority, agent);

        return ResponseEntity.ok(new ApiResponse<>(true, "Priority escalated successfully", ticket));
    }

    @PutMapping("/{id}/start")
    @PreAuthorize("hasRole('ENGINEER')")
    public ResponseEntity<ApiResponse<Ticket>> startProgress(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmailFromToken(authHeader);
        User engineer = userService.getUserByEmail(email);
        Ticket ticket = ticketService.startProgress(id, engineer);

        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket progress started", ticket));
    }

    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasAnyRole('ENGINEER', 'ADMIN', 'SERVICE_DESK')")
    public ResponseEntity<ApiResponse<Ticket>> resolveTicket(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody(required = false) UpdateResolutionNotesRequest request) {

        String email = extractEmailFromToken(authHeader);
        User user = userService.getUserByEmail(email);
        String notes = (request != null) ? request.getResolutionNotes() : null;
        Ticket ticket = ticketService.resolveTicket(id, user, notes);

        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket resolved", ticket));
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<ApiResponse<Ticket>> closeTicket(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmailFromToken(authHeader);
        User user = userService.getUserByEmail(email);
        Ticket ticket = ticketService.closeTicket(id, user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket closed", ticket));
    }

    // ===== AUDIT =====

    @GetMapping("/{id}/audit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLog(@PathVariable Long id) {
        List<AuditLog> logs = ticketService.getTicketAuditLog(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Audit logs retrieved", logs));
    }

    // ===== COMMENTS =====

    @GetMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<Comment>>> getComments(@PathVariable Long id) {
        List<Comment> comments = commentService.getCommentsByTicketId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comments retrieved", comments));
    }

    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<Comment>> addComment(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmailFromToken(authHeader);
        User user = userService.getUserByEmail(email);
        Comment comment = commentService.addComment(id, body.get("content"), user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment added", comment));
    }

    // ===== DASHBOARD STATS =====

    @GetMapping("/dashboard/stats")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> getDashboardStats(
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmailFromToken(authHeader);
        User user = userService.getUserByEmail(email);
        var stats = ticketService.getDashboardStatsByRole(user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard stats", stats));
    }

    // ===== HEALTH =====

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket service running", "OK"));
    }

    // ===== USERS FOR ASSIGNMENT =====

    @GetMapping("/engineers")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_DESK')")
    public ResponseEntity<ApiResponse<List<User>>> getEngineers() {
        List<User> engineers = ticketService.getEngineers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Engineers retrieved", engineers));
    }

    // ===== HELPER =====

    private String extractEmailFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtUtil.extractEmail(authHeader.substring(7));
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }
}
