package com.itsm.itsmsystem.model.entity;

import com.itsm.itsmsystem.enums.IssueCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    @Enumerated(EnumType.STRING)
    private IssueCategory name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "default_sla_hours_high", nullable = false)
    private Integer defaultSlaHoursHigh;

    @Column(name = "default_sla_hours_medium", nullable = false)
    private Integer defaultSlaHoursMedium;

    @Column(name = "default_sla_hours_low", nullable = false)
    private Integer defaultSlaHoursLow;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_engineer_id")
    private User assignedEngineer; // Default engineer for this category
}
