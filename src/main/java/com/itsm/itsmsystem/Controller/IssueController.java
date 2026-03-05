package com.itsm.itsmsystem.controller;

import com.itsm.itsmsystem.dto.ApiResponse;
import com.itsm.itsmsystem.dto.IssueRequest;
import com.itsm.itsmsystem.dto.IssueResponse;
import com.itsm.itsmsystem.model.entity.Issue;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.security.JwtUtil;
import com.itsm.itsmsystem.service.IssueService;
import com.itsm.itsmsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Issue API endpoints.
 * Faculty can create issues which auto-create tickets.
 */
@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
public class IssueController {

    private final IssueService issueService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Create new issue (Faculty only)
     * Auto-creates a linked ticket
     */
    @PostMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> createIssue(
            @Valid @RequestBody IssueRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            log.info("Received issue creation request: {}", request.getTitle());
            
            String email = extractEmailFromToken(authHeader);
            User faculty = userService.getUserByEmail(email);
            
            log.info("Creating issue for faculty: {}", faculty.getEmail());
            IssueResponse response = issueService.createIssue(request, faculty);
            
            log.info("Issue created successfully. Ticket ID: {}", response.getTicketId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("Error creating issue: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(IssueResponse.builder()
                    .message("Failed to create issue: " + e.getMessage())
                    .status("ERROR")
                    .build());
        }
    }

    /**
     * Get my issues (paginated)
     */
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

    /**
     * Get single issue
     */
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
