package com.itsm.itsmsystem.controller;

import com.itsm.itsmsystem.dto.ApiResponse;
import com.itsm.itsmsystem.dto.DashboardStatsDto;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.security.JwtUtil;
import com.itsm.itsmsystem.service.DashboardService;
import com.itsm.itsmsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Dashboard API endpoints for role-based statistics.
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getAdminDashboard() {
        DashboardStatsDto stats = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin dashboard stats", stats));
    }

    @GetMapping("/engineer")
    @PreAuthorize("hasRole('ENGINEER')")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getEngineerDashboard(
            @RequestHeader("Authorization") String authHeader) {
        String email = extractEmailFromToken(authHeader);
        User engineer = userService.getUserByEmail(email);
        DashboardStatsDto stats = dashboardService.getEngineerDashboard(engineer);
        return ResponseEntity.ok(new ApiResponse<>(true, "Engineer dashboard stats", stats));
    }

    @GetMapping("/faculty")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getFacultyDashboard(
            @RequestHeader("Authorization") String authHeader) {
        String email = extractEmailFromToken(authHeader);
        User faculty = userService.getUserByEmail(email);
        DashboardStatsDto stats = dashboardService.getFacultyDashboard(faculty);
        return ResponseEntity.ok(new ApiResponse<>(true, "Faculty dashboard stats", stats));
    }

    private String extractEmailFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtUtil.extractEmail(authHeader.substring(7));
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }
}
