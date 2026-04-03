package com.itsm.itsmsystem.Controller;

import com.itsm.itsmsystem.dto.ApiResponse;
import com.itsm.itsmsystem.model.entity.SLARule;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.security.JwtUtil;
import com.itsm.itsmsystem.service.SLAService;
import com.itsm.itsmsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sla-rules")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class SLAController {

    private final SLAService slaService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SLARule>>> getAllSLARules() {
        return ResponseEntity.ok(new ApiResponse<>(true, "SLA rules", slaService.getAllSLARules()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SLARule>> getSLARuleById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "SLA rule", slaService.getSLARuleById(id)));
    }

    @PutMapping("/{priority}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SLARule>> updateSLARule(
            @PathVariable String priority,
            @RequestBody Map<String, Integer> body,
            @RequestHeader("Authorization") String authHeader) {

        Integer maxHours = body.get("maxHours");
        if (maxHours == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "maxHours is required"));
        }

        String email = jwtUtil.extractEmail(authHeader.substring(7));
        User admin = userService.getUserByEmail(email);

        SLARule updated = slaService.updateSLARule(priority.toUpperCase(), maxHours, admin);
        return ResponseEntity.ok(new ApiResponse<>(true, "SLA rule updated", updated));
    }
}
