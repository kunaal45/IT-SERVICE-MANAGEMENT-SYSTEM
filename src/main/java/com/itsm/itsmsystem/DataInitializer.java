package com.itsm.itsmsystem;

import com.itsm.itsmsystem.enums.IssueCategory;
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.enums.TicketPriority;
import com.itsm.itsmsystem.enums.TicketStatus;
import com.itsm.itsmsystem.model.entity.SLARule;
import com.itsm.itsmsystem.model.entity.Ticket;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.AuditLogRepository;
import com.itsm.itsmsystem.repository.CommentRepository;
import com.itsm.itsmsystem.repository.SLARepository;
import com.itsm.itsmsystem.repository.TicketRepository;
import com.itsm.itsmsystem.repository.UserRepository;
import com.itsm.itsmsystem.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itsm.itsmsystem.enums.EngineerLevel;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final AuditService auditService;
    private final AuditLogRepository auditLogRepository;
    private final CommentRepository commentRepository;
    private final SLARepository slaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            initUsers();
            cleanupLegacyUsers();
            initSLARules();

            if (ticketRepository.count() == 0) {
                initTickets();
            }
            printStartupBanner();
        } catch (Exception e) {
            System.err.println("❌ CRITICAL ERROR DURING DATA INITIALIZATION:");
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    private void cleanupLegacyUsers() {
        String[] legacyEmails = {
                // Old system users (replaced by system.admin@itsm.com, service.desk.agent@itsm.com)
                "admin@itsm.com", "desk@itsm.com",
                // Other old legacy users
                "hardware@itsm.com", "network@itsm.com", "software@itsm.com",
                "infra@itsm.com", "facilities@itsm.com", "faculty@itsm.com", "prof@itsm.com"
        };
        for (String email : legacyEmails) {
            userRepository.findByEmail(email).ifPresent(user -> {
                System.out.println("Processing cleanup for user: " + email);

                // 1. Tickets where this user is assigned: Reset them to UNASSIGNED
                ticketRepository.findByAssignedToId(user.getId()).forEach(t -> {
                    t.setAssignedTo(null);
                    t.setStatus(TicketStatus.OPEN);
                    ticketRepository.save(t);
                });

                // 2. Tickets where this user is the CREATOR: Migrated to new System Admin
                // We find the new system admin to take ownership of these records
                User systemAdmin = userRepository.findByEmail("system.admin@itsm.com").orElse(null);
                ticketRepository.findByCreatedById(user.getId()).forEach(t -> {
                    if (systemAdmin != null) {
                        t.setCreatedBy(systemAdmin);
                        ticketRepository.save(t);
                    } else {
                        // Fallback: If system admin not found, we delete to avoid orphan records
                        auditLogRepository.deleteByTicketId(t.getId());
                        commentRepository.deleteByTicketId(t.getId());
                        ticketRepository.delete(t);
                    }
                });

                // 3. Independent Audit Logs and Comments by this user
                auditLogRepository.deleteByUserId(user.getId());
                commentRepository.deleteByUserId(user.getId());

                // 4. Finally delete the user
                userRepository.delete(user);
                System.out.println("🗑️ Successfully removed legacy user: " + email);
            });
        }
    }

    private void initUsers() {
        // Essential System Users
        seedSystemUser("System Admin", Role.ADMIN);
        seedSystemUser("Service Desk Agent", Role.SERVICE_DESK);

        // Required Engineers with Category Mapping

        // Group 1: INFRASTRUCTURE, ATTENDANCE_SYSTEM, RESOURCE_REQUEST
        Set<IssueCategory> group1 = EnumSet.of(IssueCategory.INFRASTRUCTURE, IssueCategory.ATTENDANCE_SYSTEM,
                IssueCategory.RESOURCE_REQUEST);
        seedEngineer("Pradeep", EngineerLevel.SENIOR, group1);
        seedEngineer("Selva", EngineerLevel.JUNIOR, group1);

        // Group 2: NETWORK, PORTAL_WEBSITE, ACCESS
        Set<IssueCategory> group2 = EnumSet.of(IssueCategory.NETWORK, IssueCategory.PORTAL_WEBSITE,
                IssueCategory.ACCESS);
        seedEngineer("Vikram", EngineerLevel.SENIOR, group2);
        seedEngineer("Abi", EngineerLevel.JUNIOR, group2);

        // Group 3: HARDWARE, SOFTWARE
        Set<IssueCategory> group3 = EnumSet.of(IssueCategory.HARDWARE, IssueCategory.SOFTWARE);
        seedEngineer("Priya", EngineerLevel.SENIOR, group3);
        seedEngineer("Rohit", EngineerLevel.JUNIOR, group3);

        // Required Faculty Users
        seedFaculty("Rajesh");
        seedFaculty("Rahul");
    }

    private void seedSystemUser(String name, Role role) {
        String email = name.toLowerCase().replace(" ", ".") + "@itsm.com";
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("Welcome@123"));
        user.setRole(role);
        User saved = userRepository.save(user);
        System.out.println("👤 System User Initialized: " + email);
        auditService.logAction("USER_SEEDED", saved, null, "System user seeded: " + email, null, email);
    }

    private void seedEngineer(String name, EngineerLevel level, Set<IssueCategory> categories) {
        String email = name.toLowerCase() + "@itsm.com";
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("Engineer@123"));
        user.setRole(Role.ENGINEER);
        user.setEngineerLevel(level);
        user.setSpecializedCategories(categories);
        User saved = userRepository.save(user);
        System.out.println("🛠️ Engineer Initialized: " + email);
        auditService.logAction("USER_SEEDED", saved, null, "Engineer seeded: " + email, null, email);
    }

    private void seedFaculty(String name) {
        String email = name.toLowerCase() + "@itsm.com";
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("Faculty@123"));
        user.setRole(Role.FACULTY);
        User saved = userRepository.save(user);
        System.out.println("🎓 Faculty Initialized: " + email);
        auditService.logAction("USER_SEEDED", saved, null, "Faculty seeded: " + email, null, email);
    }

    private void initSLARules() {
        if (slaRepository.count() > 0)
            return;

        createSLARule("HIGH", 4);
        createSLARule("MEDIUM", 12);
        createSLARule("LOW", 24);
    }

    private void createSLARule(String priority, int maxHours) {
        SLARule rule = new SLARule();
        rule.setPriority(priority);
        rule.setMaxHours(maxHours);
        slaRepository.save(rule);
    }

    private void initTickets() {
        // Reference users
        User selva = userRepository.findByEmail("selva@itsm.com").orElse(null);
        User rajesh = userRepository.findByEmail("rajesh@itsm.com").orElse(null);

        if (selva != null && rajesh != null) {
            seedTicket("Office Power Outage", "Socket in office 205 is sparking",
                    IssueCategory.INFRASTRUCTURE, TicketPriority.CRITICAL, "Office 205",
                    rajesh, TicketStatus.ASSIGNED, selva);
        }
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

        auditService.logAction("TICKET_CREATED", creator, saved.getId(), 
            "Ticket created: " + title, null, status.name());

        if (assignedTo != null) {
            auditService.logAction("TICKET_ASSIGNED", assignedTo, saved.getId(), 
                "Assigned to " + assignedTo.getName(), null, assignedTo.getName());
        }
    }

    private void printStartupBanner() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         ✅ ITSM SYSTEM STARTED SUCCESSFULLY ✅             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\n📊 SYSTEM READY: Users and Structures Initialized.");
        System.out.println("\n🔐 DYNAMIC CREDENTIALS (Email pattern: name@itsm.com):");
        System.out.println("   ADMIN:        system.admin@itsm.com (Welcome@123)");
        System.out.println("   FACULTY:      rajesh@itsm.com       (Faculty@123)");
        System.out.println("   JR ENGINEER:  selva@itsm.com        (Engineer@123)");
        System.out.println("   SR ENGINEER:  pradeep@itsm.com      (Engineer@123)");
        System.out.println("\n📍 Access at: http://localhost:8080\n");
    }
}
