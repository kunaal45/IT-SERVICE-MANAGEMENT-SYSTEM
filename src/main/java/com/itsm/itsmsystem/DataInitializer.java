package com.itsm.itsmsystem;

import com.itsm.itsmsystem.enums.IssueCategory;
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.enums.TicketPriority;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.model.entity.Issue;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.AuditLogRepository;
import com.itsm.itsmsystem.repository.IssueRepository;
import com.itsm.itsmsystem.repository.TicketRepository;
import com.itsm.itsmsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Complete database seeder for ITSM system.
 * Creates demo users, issues, tickets, and audit logs on startup.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final TicketRepository ticketRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Prevent duplicate seeding
        if (userRepository.count() > 0) {
            return;
        }
        initializeUsers();
        initializeIssuesAndTickets();
        printStartupBanner();
    }

    private void initializeUsers() {
        if (userRepository.count() > 0) return;

        // Admin User
        User admin = new User();
        admin.setEmail("admin@itsm.com");
        admin.setName("System Admin");
        admin.setRole(Role.ADMIN);
        admin.setPassword(passwordEncoder.encode("admin123"));
        userRepository.save(admin);

        // Engineer 1
        User engineer1 = new User();
        engineer1.setEmail("engineer@itsm.com");
        engineer1.setName("John Engineer");
        engineer1.setRole(Role.ENGINEER);
        engineer1.setPassword(passwordEncoder.encode("eng123"));
        userRepository.save(engineer1);

        // Engineer 2
        User engineer2 = new User();
        engineer2.setEmail("tech@itsm.com");
        engineer2.setName("Tech Support");
        engineer2.setRole(Role.ENGINEER);
        engineer2.setPassword(passwordEncoder.encode("eng123"));
        userRepository.save(engineer2);

        // Faculty 1
        User faculty = new User();
        faculty.setEmail("faculty@itsm.com");
        faculty.setName("Dr. Faculty Member");
        faculty.setRole(Role.FACULTY);
        faculty.setPassword(passwordEncoder.encode("faculty123"));
        userRepository.save(faculty);

        // Faculty 2
        User faculty2 = new User();
        faculty2.setEmail("prof@itsm.com");
        faculty2.setName("Prof. Science");
        faculty2.setRole(Role.FACULTY);
        faculty2.setPassword(passwordEncoder.encode("faculty123"));
        userRepository.save(faculty2);
    }

    private void initializeIssuesAndTickets() {
        if (issueRepository.count() > 0) return;

        // Get users - they MUST exist since we created them in initializeUsers()
        User admin = userRepository.findByEmail("admin@itsm.com")
            .orElseThrow(() -> new RuntimeException("Admin user must be created first"));
        User engineer1 = userRepository.findByEmail("engineer@itsm.com")
            .orElseThrow(() -> new RuntimeException("Engineer1 user must be created first"));
        User engineer2 = userRepository.findByEmail("tech@itsm.com")
            .orElseThrow(() -> new RuntimeException("Engineer2 user must be created first"));
        User faculty = userRepository.findByEmail("faculty@itsm.com")
            .orElseThrow(() -> new RuntimeException("Faculty user must be created first"));
        User faculty2 = userRepository.findByEmail("prof@itsm.com")
            .orElseThrow(() -> new RuntimeException("Faculty2 user must be created first"));

        // Create 10 demo issues and auto-create tickets
        createIssueAndTicket("Lab Computer Not Starting", 
            "Computer in lab 101 won't boot up", IssueCategory.HARDWARE, TicketPriority.HIGH, 
            "Lab 101", faculty, TicketStatus.OPEN, null, engineer1);

        createIssueAndTicket("WiFi Speed Degradation",
            "Slow internet connection in reading room", IssueCategory.NETWORK, TicketPriority.HIGH,
            "Reading Room", faculty, TicketStatus.ASSIGNED, engineer2, engineer2);

        createIssueAndTicket("Printer Error Code E02",
            "Printer displaying error code E02", IssueCategory.HARDWARE, TicketPriority.MEDIUM,
            "Admin Office", faculty2, TicketStatus.IN_PROGRESS, engineer1, engineer1);

        createIssueAndTicket("Office 365 Login Issues",
            "Cannot access Office 365 applications", IssueCategory.SOFTWARE, TicketPriority.HIGH,
            "Admin Block", faculty, TicketStatus.ASSIGNED, engineer2, engineer2);

        createIssueAndTicket("VPN Connection Timeout",
            "VPN keeps disconnecting", IssueCategory.NETWORK, TicketPriority.MEDIUM,
            "Remote", faculty2, TicketStatus.IN_PROGRESS, engineer1, engineer1);

        createIssueAndTicket("Power Outlet Damaged",
            "Socket in office sparking", IssueCategory.OTHER, TicketPriority.CRITICAL,
            "Office 205", faculty, TicketStatus.RESOLVED, engineer2, engineer2);

        createIssueAndTicket("Monitor Resolution Low",
            "Monitor not displaying correct resolution", IssueCategory.HARDWARE, TicketPriority.LOW,
            "Lab 204", faculty2, TicketStatus.CLOSED, engineer1, engineer1);

        createIssueAndTicket("Software License Expired",
            "Adobe Creative Suite license expired", IssueCategory.SOFTWARE, TicketPriority.MEDIUM,
            "Design Lab", faculty, TicketStatus.RESOLVED, engineer2, engineer2);

        createIssueAndTicket("Network Cable Loose",
            "Network cable disconnecting frequently", IssueCategory.NETWORK, TicketPriority.MEDIUM,
            "Server Room", faculty2, TicketStatus.OPEN, null, engineer1);

        createIssueAndTicket("Database Slow Performance",
            "Database queries taking too long", IssueCategory.SOFTWARE, TicketPriority.HIGH,
            "Server", faculty, TicketStatus.IN_PROGRESS, engineer1, engineer1);
    }

    private void createIssueAndTicket(String title, String description, IssueCategory category,
                                     TicketPriority priority, String location, User creator,
                                     TicketStatus ticketStatus, User assignedTo, User assignedEngineer) {
        // Create issue
        Issue issue = new Issue();
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setCategory(category);
        issue.setPriority(priority);
        issue.setLocation(location);
        issue.setCreatedBy(creator);
        Issue savedIssue = issueRepository.save(issue);

        // Auto-create ticket
        Ticket ticket = new Ticket();
        ticket.setIssue(savedIssue);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCategory(category);
        ticket.setPriority(priority);
        ticket.setLocation(location);
        ticket.setStatus(ticketStatus);
        ticket.setCreatedBy(creator);
        ticket.setAssignedTo(assignedTo);

        // Set timestamps based on status
        if (ticketStatus == TicketStatus.IN_PROGRESS) {
            ticket.setUpdatedAt(LocalDateTime.now().minusHours(3));
        } else if (ticketStatus == TicketStatus.RESOLVED) {
            ticket.setUpdatedAt(LocalDateTime.now().minusHours(2));
            ticket.setResolvedAt(LocalDateTime.now().minusHours(1));
        } else if (ticketStatus == TicketStatus.CLOSED) {
            ticket.setUpdatedAt(LocalDateTime.now().minusDays(1));
            ticket.setResolvedAt(LocalDateTime.now().minusDays(1).minusHours(2));
            ticket.setClosedAt(LocalDateTime.now().minusDays(1).minusHours(1));
        } else {
            ticket.setUpdatedAt(LocalDateTime.now());
        }

        Ticket savedTicket = ticketRepository.save(ticket);

        // Create audit log
        AuditLog log = AuditLog.builder()
            .action("ISSUE_CREATED")
            .ticket(savedTicket)
            .user(creator)
            .details("Issue created: " + title)
            .createdAt(LocalDateTime.now())
            .build();
        auditLogRepository.save(log);

        if (assignedEngineer != null) {
            AuditLog assignLog = AuditLog.builder()
                .action("TICKET_ASSIGNED")
                .ticket(savedTicket)
                .user(assignedEngineer)
                .details("Assigned to " + assignedEngineer.getName())
                .createdAt(LocalDateTime.now())
                .build();
            auditLogRepository.save(assignLog);
        }
    }

    private void printStartupBanner() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                            ║");
        System.out.println("║          ✅ ITSM SYSTEM STARTED SUCCESSFULLY ✅            ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\n📊 DEMO DATA LOADED:");
        System.out.println("   ✓ 5 Users (1 Admin, 2 Engineers, 2 Faculty)");
        System.out.println("   ✓ 10 Issues");
        System.out.println("   ✓ 10 Tickets (various statuses)");
        System.out.println("   ✓ Audit logs for all actions");
        System.out.println("\n🔐 LOGIN CREDENTIALS:");
        System.out.println("   ADMIN:    admin@itsm.com / admin123");
        System.out.println("   ENGINEER: engineer@itsm.com / eng123");
        System.out.println("   FACULTY:  faculty@itsm.com / faculty123");
        System.out.println("\n📍 Access at: http://localhost:8080");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
}
