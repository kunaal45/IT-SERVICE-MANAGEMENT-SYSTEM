package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.exception.ResourceNotFoundException;
import com.itsm.itsmsystem.model.entity.SLARule;
import com.itsm.itsmsystem.repository.SLARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SLAService {

    private final SLARepository slaRepository;

    public List<SLARule> getAllSLARules() {
        return slaRepository.findAll();
    }

    public SLARule getSLARuleById(Long id) {
        return slaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SLA Rule not found"));
    }

    public SLARule updateSLARule(String priority, Integer maxHours) {
        SLARule rule = slaRepository.findByPriority(priority)
                .orElseThrow(() -> new ResourceNotFoundException("SLA Rule not found for priority: " + priority));
        rule.setMaxHours(maxHours);
        return slaRepository.save(rule);
    }
}
