package com.itsm.itsmsystem.Controller;

import com.itsm.itsmsystem.dto.ApiResponse;
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @GetMapping("/engineers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getEngineers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Engineers", userService.getUsersByRole(Role.ENGINEER)));
    }
}
