package com.itsm.itsmsystem.Controller;

import com.itsm.itsmsystem.dto.ApiResponse;

import com.itsm.itsmsystem.model.entity.Issue;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.security.JwtUtil;
import com.itsm.itsmsystem.service.IssueService;
import com.itsm.itsmsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Issue API endpoints.
 * Faculty can create issues which auto-create tickets.
 */
@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
public class IssueController {

}
