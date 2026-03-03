package com.itsm.itsmsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sla_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SLARule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "priority", nullable = false, unique = true, length = 50)
    private String priority; // HIGH, MEDIUM, LOW

    @Column(name = "max_hours", nullable = false)
    private Integer maxHours; // Maximum hours to resolve
}