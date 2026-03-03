package com.itsm.itsmsystem.Controller;

import com.itsm.itsmsystem.model.entity.SLARule;
import com.itsm.itsmsystem.service.SLAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sla-rules")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SLAController {
    
    private final SLAService slaService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SLARule>> getAllSLARules() {
        return ResponseEntity.ok(slaService.getAllSLARules());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SLARule> getSLARuleById(@PathVariable Long id) {
        return ResponseEntity.ok(slaService.getSLARuleById(id));
    }
}
