package com.itsm.itsmsystem.repository;

import com.itsm.itsmsystem.model.entity.SLARule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SLARepository extends JpaRepository<SLARule, Long> {
    Optional<SLARule> findByPriority(String priority);
}