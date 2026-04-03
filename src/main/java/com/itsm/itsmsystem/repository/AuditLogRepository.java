package com.itsm.itsmsystem.repository;

import com.itsm.itsmsystem.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTicketIdOrderByTimestampDesc(Long ticketId);

    List<AuditLog> findByUserIdOrderByTimestampDesc(Long userId);

    List<AuditLog> findByActionTypeOrderByTimestampDesc(String actionType);

    List<AuditLog> findAllByOrderByTimestampDesc();

    List<AuditLog> findTop10ByOrderByTimestampDesc();

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

    @Modifying
    @Transactional
    void deleteByTicketId(Long ticketId);
}
