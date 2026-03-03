package com.itsm.itsmsystem.dto;

import com.itsm.itsmsystem.enums.TicketPriority;
import com.itsm.itsmsystem.enums.IssueCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;
    
    @NotNull(message = "Priority is required")
    private TicketPriority priority;
    
    @NotNull(message = "Category is required")
    private IssueCategory category;
    
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    private String location;
}
