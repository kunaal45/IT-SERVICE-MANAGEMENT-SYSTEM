package com.itsm.itsmsystem.Controller;

import com.itsm.itsmsystem.dto.ApiResponse;
import com.itsm.itsmsystem.dto.CreateUserRequest;
import com.itsm.itsmsystem.enums.EngineerLevel;
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ===== LIST =====

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "All users", userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "User", userService.getUserById(id)));
    }

    @GetMapping("/by-role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Users by role", userService.getUsersByRole(role)));
    }

    @GetMapping("/engineers")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_DESK')")
    public ResponseEntity<ApiResponse<List<User>>> getEngineers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Engineers", userService.getUsersByRole(Role.ENGINEER)));
    }

    // ===== CREATE =====

    /**
     * POST /api/users
     * Admin creates a new user (any role: ENGINEER, FACULTY, SERVICE_DESK, ADMIN)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody CreateUserRequest request) {
        User created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User created successfully", created));
    }

    // ===== UPDATE =====

    /**
     * PUT /api/users/{id}/role
     * Admin changes a user's role. Body: { "role": "ENGINEER" }
     */
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        Role newRole = Role.valueOf(body.get("role").toUpperCase());
        User updated = userService.updateUserRole(id, newRole);
        return ResponseEntity.ok(new ApiResponse<>(true, "User role updated to " + newRole, updated));
    }

    /**
     * PUT /api/users/{id}/engineer-level
     * Admin updates engineer level. Body: { "level": "SENIOR" }
     */
    @PutMapping("/{id}/engineer-level")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> updateEngineerLevel(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        EngineerLevel level = EngineerLevel.valueOf(body.get("level").toUpperCase());
        User updated = userService.updateEngineerDetails(id, level);
        return ResponseEntity.ok(new ApiResponse<>(true, "Engineer level updated to " + level, updated));
    }

    // ===== DELETE =====

    /**
     * DELETE /api/users/{id}
     * Admin permanently deletes a user.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully"));
    }
}
