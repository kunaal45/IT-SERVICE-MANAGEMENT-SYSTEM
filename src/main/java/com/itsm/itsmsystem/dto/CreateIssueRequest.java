package com.itsm.itsmsystem.dto;

import com.itsm.itsmsystem.enums.IssueCategory;
import com.itsm.itsmsystem.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateIssueRequest {
    
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;
    
    @NotNull(message = "Category is required")
    private IssueCategory category;
    
    @NotNull(message = "Priority is required")
    private TicketPriority priority;
    
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    private String location;
}
