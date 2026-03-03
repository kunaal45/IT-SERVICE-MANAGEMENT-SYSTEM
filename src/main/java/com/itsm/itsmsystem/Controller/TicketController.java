package com.itsm.itsmsystem.controller;

import com.itsm.itsmsystem.dto.ApiResponse;
import com.itsm.itsmsystem.dto.AssignTicketRequest;
import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.security.JwtUtil;
import com.itsm.itsmsystem.service.TicketService;
import com.itsm.itsmsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Ticket API endpoints with pagination and role-based access.
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // ===== LISTING =====

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<Ticket>>> getAllTickets(Pageable pageable) {
        Page<Ticket> tickets = ticketService.getAllTickets(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "All tickets retrieved", tickets));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ENGINEER')")
    public ResponseEntity<ApiResponse<Page<Ticket>>> getMyTickets(
            @RequestHeader("Authorization") String authHeader,
            Pageable pageable) {
        
        String email = extractEmailFromToken(authHeader);
        User engineer = userService.getUserByEmail(email);
        Page<Ticket> tickets = ticketService.getTicketsByAssignee(engineer, pageable);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Your tickets retrieved", tickets));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Ticket>> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket retrieved", ticket));
    }

    // ===== WORKFLOW OPERATIONS =====

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Ticket>> assignTicket(
            @PathVariable Long id,
            @Valid @RequestBody AssignTicketRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        String email = extractEmailFromToken(authHeader);
        User admin = userService.getUserByEmail(email);
        Ticket ticket = ticketService.assignTicket(id, request, admin);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket assigned successfully", ticket));
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
    @PreAuthorize("hasAnyRole('ENGINEER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Ticket>> resolveTicket(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        
        String email = extractEmailFromToken(authHeader);
        User user = userService.getUserByEmail(email);
        Ticket ticket = ticketService.resolveTicket(id, user);
        
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

    // ===== HEALTH =====

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Ticket service running", "OK"));
    }

    // ===== HELPER =====

    private String extractEmailFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtUtil.extractEmail(authHeader.substring(7));
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }
}
