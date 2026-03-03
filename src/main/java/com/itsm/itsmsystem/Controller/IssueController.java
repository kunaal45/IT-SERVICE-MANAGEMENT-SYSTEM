package com.itsm.itsmsystem.controller;

import com.itsm.itsmsystem.dto.ApiResponse;
import com.itsm.itsmsystem.dto.CreateIssueRequest;
import com.itsm.itsmsystem.model.entity.Issue;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.security.JwtUtil;
import com.itsm.itsmsystem.service.IssueService;
import com.itsm.itsmsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Issue API endpoints.
 * Faculty can create issues which auto-create tickets.
 */
@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<Ticket>> createIssue(
            @Valid @RequestBody CreateIssueRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        String email = extractEmailFromToken(authHeader);
        User faculty = userService.getUserByEmail(email);
        Ticket ticket = issueService.createIssue(request, faculty);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "Issue created and ticket auto-generated", ticket));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<Page<Issue>>> getMyIssues(
            @RequestHeader("Authorization") String authHeader,
            Pageable pageable) {
        
        String email = extractEmailFromToken(authHeader);
        User faculty = userService.getUserByEmail(email);
        Page<Issue> issues = issueService.getMyIssues(faculty, pageable);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Your issues retrieved", issues));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Issue>> getIssue(@PathVariable Long id) {
        Issue issue = issueService.getIssue(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Issue retrieved", issue));
    }

    private String extractEmailFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtUtil.extractEmail(authHeader.substring(7));
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }
}
