package com.itsm.itsmsystem.repository;

import com.itsm.itsmsystem.model.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByCreatedById(Long userId);
    Page<Issue> findByCreatedById(Long userId, Pageable pageable);
}
