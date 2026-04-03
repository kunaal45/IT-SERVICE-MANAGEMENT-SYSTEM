package com.itsm.itsmsystem;

import com.itsm.itsmsystem.repository.UserRepository;
import com.itsm.itsmsystem.repository.TicketRepository;
import com.itsm.itsmsystem.repository.CommentRepository;
import com.itsm.itsmsystem.repository.AuditLogRepository;
import com.itsm.itsmsystem.repository.SLARepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItsmSystemApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private SLARepository slaRepository;

    @Test
    void contextLoads() {
        // Test that Spring context loads successfully
        assertNotNull(userRepository);
        assertNotNull(ticketRepository);
        assertNotNull(commentRepository);
        assertNotNull(auditLogRepository);
        assertNotNull(slaRepository);
    }

    @Test
    void testDatabaseConnection() {
        // Test that database connection works
        assertTrue(userRepository.findAll().size() >= 0, "User repository should connect to database");
    }

    @Test
    void testUserTableExists() {
        // Test that users table has sample data
        long userCount = userRepository.count();
        assertTrue(userCount >= 5, "Should have at least 5 sample users in database");
    }

    @Test
    void testTicketTableExists() {
        // Test that tickets table has sample data
        long ticketCount = ticketRepository.count();
        assertEquals(10, ticketCount, "Should have 10 sample tickets in database");
    }

    @Test
    void testCommentTableExists() {
        // Test that comments table
        long commentCount = commentRepository.count();
        assertTrue(commentCount >= 0, "Comment repository should exist");
    }

    @Test
    void testAuditLogTableExists() {
        // Test that audit_logs table has sample data
        long auditLogCount = auditLogRepository.count();
        // 10 tickets created + 6 assigned during seed = 16 logs
        assertTrue(auditLogCount >= 10, "Should have at least 10 audit logs in database");
    }

    @Test
    void testSLARuleTableExists() {
        // Test that sla_rules table has sample data
        long slaCount = slaRepository.count();
        assertEquals(3, slaCount, "Should have 3 SLA rules in database");
    }

    @Test
    void testAdminUserExists() {
        var adminUser = userRepository.findByEmail("admin@itsm.com");
        assertTrue(adminUser.isPresent(), "Admin user should exist in database");
        assertEquals("ADMIN", adminUser.get().getRole().name(), "Admin user should have ADMIN role");
    }

    @Test
    void testEngineerUserExists() {
        // Test that engineer user exists in database
        var engineer = userRepository.findByEmail("hardware@itsm.com");
        assertTrue(engineer.isPresent(), "Engineer user should exist in database");
        assertEquals("ENGINEER", engineer.get().getRole().name(), "Engineer should have ENGINEER role");
    }

    @Test
    void testStudentUserExists() {
        // Test that faculty user exists in database
        var faculty = userRepository.findByEmail("faculty@itsm.com");
        assertTrue(faculty.isPresent(), "Faculty user should exist in database");
        assertEquals("FACULTY", faculty.get().getRole().name(), "Faculty should have FACULTY role");
    }

    @Test
    void testSLARulesConfiguration() {
        // Test that SLA rules are correctly configured
        var highSLA = slaRepository.findByPriority("HIGH");
        assertTrue(highSLA.isPresent(), "HIGH priority SLA rule should exist");
        assertEquals(4, highSLA.get().getMaxHours(), "HIGH priority should be 4 hours");

        var mediumSLA = slaRepository.findByPriority("MEDIUM");
        assertTrue(mediumSLA.isPresent(), "MEDIUM priority SLA rule should exist");
        assertEquals(12, mediumSLA.get().getMaxHours(), "MEDIUM priority should be 12 hours");

        var lowSLA = slaRepository.findByPriority("LOW");
        assertTrue(lowSLA.isPresent(), "LOW priority SLA rule should exist");
        assertEquals(24, lowSLA.get().getMaxHours(), "LOW priority should be 24 hours");
    }
}