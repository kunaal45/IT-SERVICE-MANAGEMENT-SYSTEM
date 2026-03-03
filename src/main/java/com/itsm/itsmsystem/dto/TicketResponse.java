package com.itsm.itsmsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String category;
    private String location;
    private UserDto createdBy;
    private UserDto assignedTo;
    private String createdAt;
    private String updatedAt;
    private String resolvedAt;
    private String closedAt;
}
