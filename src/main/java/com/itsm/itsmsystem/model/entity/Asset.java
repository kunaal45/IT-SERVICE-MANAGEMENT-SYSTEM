package com.itsm.itsmsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String assetCode;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 50)
    private String category; // LAPTOP, DESKTOP, PRINTER, MONITOR, etc.

    @Column(length = 50)
    private String status; // AVAILABLE, ASSIGNED, MAINTENANCE, RETIRED

    @Column(columnDefinition = "TEXT")
    private String location;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
