package com.itsm.itsmsystem;

import com.itsm.itsmsystem.enums.IssueCategory;
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.enums.TicketPriority;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.model.entity.AuditLog;
import com.itsm.itsmsystem.model.entity.SLARule;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.AuditLogRepository;
import com.itsm.itsmsystem.repository.SLARepository;
import com.itsm.itsmsystem.repository.TicketRepository;
import com.itsm.itsmsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final AuditLogRepository auditLogRepository;
    private final SLARepository slaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            printStartupBanner();
            return;
        }

        initUsers();
        initSLARules();
        initTickets();
        printStartupBanner();
    }

    private void initUsers() {
        createUser("System Admin", "admin@itsm.com", "admin123", Role.ADMIN);
        createUser("John Engineer", "engineer@itsm.com", "eng123", Role.ENGINEER);
        createUser("Tech Support", "tech@itsm.com", "eng123", Role.ENGINEER);
        createUser("Dr. Faculty Member", "faculty@itsm.com", "faculty123", Role.FACULTY);
        createUser("Prof. Science", "prof@itsm.com", "faculty123", Role.FACULTY);
    }

    private User createUser(String name, String email, String password, Role role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    private void initSLARules() {
        if (slaRepository.count() > 0) return;

        createSLARule("CRITICAL", 2);
        createSLARule("HIGH", 4);
        createSLARule("MEDIUM", 8);
        createSLARule("LOW", 24);
    }

    private void createSLARule(String priority, int maxHours) {
        SLARule rule = new SLARule();
        rule.setPriority(priority);
        rule.setMaxHours(maxHours);
        slaRepository.save(rule);
    }

    private void initTickets() {
        User admin = userRepository.findByEmail("admin@itsm.com").orElseThrow();
        User engineer1 = userRepository.findByEmail("engineer@itsm.com").orElseThrow();
        User engineer2 = userRepository.findByEmail("tech@itsm.com").orElseThrow();
        User faculty = userRepository.findByEmail("faculty@itsm.com").orElseThrow();
        User faculty2 = userRepository.findByEmail("prof@itsm.com").orElseThrow();

        seedTicket("Lab Computer Not Starting", "Computer in lab 101 won't boot up",
                IssueCategory.HARDWARE, TicketPriority.HIGH, "Lab 101",
                faculty, TicketStatus.OPEN, null);

        seedTicket("WiFi Speed Degradation", "Slow internet connection in reading room",
                IssueCategory.NETWORK, TicketPriority.HIGH, "Reading Room",
                faculty, TicketStatus.ASSIGNED, engineer2);

        seedTicket("Printer Error Code E02", "Printer displaying error code E02",
                IssueCategory.HARDWARE, TicketPriority.MEDIUM, "Admin Office",
                faculty2, TicketStatus.IN_PROGRESS, engineer1);

        seedTicket("Office 365 Login Issues", "Cannot access Office 365 applications",
                IssueCategory.SOFTWARE, TicketPriority.HIGH, "Admin Block",
                faculty, TicketStatus.ASSIGNED, engineer2);

        seedTicket("Portal Login Failure", "Faculty portal showing 500 error on login",
                IssueCategory.PORTAL_WEBSITE, TicketPriority.HIGH, "IT Server",
                faculty2, TicketStatus.IN_PROGRESS, engineer1);

        seedTicket("Power Outlet Damaged", "Socket in office sparking",
                IssueCategory.INFRASTRUCTURE, TicketPriority.CRITICAL, "Office 205",
                faculty, TicketStatus.RESOLVED, engineer2);

        seedTicket("Monitor Resolution Low", "Monitor not displaying correct resolution",
                IssueCategory.HARDWARE, TicketPriority.LOW, "Lab 204",
                faculty2, TicketStatus.CLOSED, engineer1);

        seedTicket("Software License Expired", "Adobe Creative Suite license expired",
                IssueCategory.SOFTWARE, TicketPriority.MEDIUM, "Design Lab",
                faculty, TicketStatus.RESOLVED, engineer2);

        seedTicket("Attendance System Error", "Biometric device not syncing attendance",
                IssueCategory.ATTENDANCE_SYSTEM, TicketPriority.HIGH, "Block C",
                faculty2, TicketStatus.OPEN, null);

        seedTicket("Database Slow Performance", "Database queries taking too long",
                IssueCategory.SOFTWARE, TicketPriority.HIGH, "Server Room",
                faculty, TicketStatus.IN_PROGRESS, engineer1);
    }

    private void seedTicket(String title, String description, IssueCategory category,
                            TicketPriority priority, String location, User creator,
                            TicketStatus status, User assignedTo) {

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCategory(category);
        ticket.setPriority(priority);
        ticket.setLocation(location);
        ticket.setStatus(status);
        ticket.setCreatedBy(creator);
        ticket.setAssignedTo(assignedTo);

        if (status == TicketStatus.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now().minusHours(1));
        } else if (status == TicketStatus.CLOSED) {
            ticket.setResolvedAt(LocalDateTime.now().minusDays(1));
            ticket.setClosedAt(LocalDateTime.now().minusHours(20));
        }

        Ticket saved = ticketRepository.save(ticket);

        AuditLog log = AuditLog.builder()
                .action("TICKET_CREATED")
                .ticket(saved)
                .user(creator)
                .details("Ticket created: " + title)
                .createdAt(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);

        if (assignedTo != null) {
            AuditLog assignLog = AuditLog.builder()
                    .action("TICKET_ASSIGNED")
                    .ticket(saved)
                    .user(assignedTo)
                    .details("Assigned to " + assignedTo.getName())
                    .createdAt(LocalDateTime.now())
                    .build();
            auditLogRepository.save(assignLog);
        }
    }

    private void printStartupBanner() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         ✅ ITSM SYSTEM STARTED SUCCESSFULLY ✅             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\n📊 DEMO DATA: 5 Users | 10 Tickets | 4 SLA Rules");
        System.out.println("\n🔐 LOGIN CREDENTIALS:");
        System.out.println("   ADMIN:    admin@itsm.com    / admin123");
        System.out.println("   ENGINEER: engineer@itsm.com / eng123");
        System.out.println("   FACULTY:  faculty@itsm.com  / faculty123");
        System.out.println("\n📍 Access at: http://localhost:8080\n");
    }
}
