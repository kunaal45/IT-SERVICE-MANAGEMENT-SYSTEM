package com.itsm.itsmsystem.repository;

import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    List<User> findByRole(Role role);

    List<User> findByRoleIn(List<Role> roles);

    @Query("SELECT u FROM User u JOIN u.specializedCategories sc WHERE u.role = 'ENGINEER' AND u.engineerLevel = :level AND sc = :category")
    List<User> findEngineerByLevelAndCategory(
            @Param("level") com.itsm.itsmsystem.enums.EngineerLevel level,
            @Param("category") com.itsm.itsmsystem.enums.IssueCategory category);
}
