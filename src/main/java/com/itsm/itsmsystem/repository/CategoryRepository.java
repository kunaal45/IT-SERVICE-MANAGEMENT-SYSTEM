package com.itsm.itsmsystem.repository;

import com.itsm.itsmsystem.enums.IssueCategory;
import com.itsm.itsmsystem.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(IssueCategory name);
}
