package com.itsm.itsmsystem.dto;

import com.itsm.itsmsystem.enums.EngineerLevel;
import com.itsm.itsmsystem.enums.IssueCategory;
import com.itsm.itsmsystem.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    // Only required if role = ENGINEER
    private EngineerLevel engineerLevel;

    // Only required if role = ENGINEER
    private Set<IssueCategory> specializedCategories;
}
