package com.itsm.itsmsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignTicketRequest {
    
    @NotNull(message = "Engineer ID is required")
    private Long engineerId;
}
