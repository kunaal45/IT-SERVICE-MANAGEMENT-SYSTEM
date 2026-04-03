package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.exception.ResourceNotFoundException;
import com.itsm.itsmsystem.model.entity.SLARule;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.SLARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class SLAService {

    private final SLARepository slaRepository;
    private final AuditService auditService;

    public List<SLARule> getAllSLARules() {
        return slaRepository.findAll();
    }

    public SLARule getSLARuleById(Long id) {
        return slaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SLA Rule not found"));
    }

    public SLARule updateSLARule(String priority, Integer maxHours, User admin) {
        Optional<SLARule> ruleOpt = slaRepository.findByPriority(priority);
        SLARule rule;
        String oldValue = "";

        if (ruleOpt.isPresent()) {
            rule = ruleOpt.get();
            oldValue = String.valueOf(rule.getMaxHours());
            rule.setMaxHours(maxHours);
        } else {
            rule = new SLARule();
            rule.setPriority(priority);
            rule.setMaxHours(maxHours);
        }

        SLARule saved = slaRepository.save(rule);

        // Log the change
        auditService.logAction(
            "SLA_CONFIG_CHANGED",
            admin,
            null,
            "SLA for priority " + priority + " updated from " + oldValue + " to " + maxHours + " hours by " + admin.getName(),
            oldValue,
            String.valueOf(maxHours)
        );

        return saved;
    }

    public int getHoursForPriority(String priority) {
        return slaRepository.findByPriority(priority)
                .map(SLARule::getMaxHours)
                .orElseGet(() -> {
                    // Fallback to defaults
                    return switch (priority.toUpperCase()) {
                        case "CRITICAL" -> 4;
                        case "HIGH" -> 4;
                        case "MEDIUM" -> 12;
                        case "LOW" -> 24;
                        default -> 24;
                    };
                });
    }
}
