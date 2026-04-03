package com.itsm.itsmsystem.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Issue API endpoints.
 * Faculty can create issues which auto-create tickets.
 */
@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class IssueController {

}
