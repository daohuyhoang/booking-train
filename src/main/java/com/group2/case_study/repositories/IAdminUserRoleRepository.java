package com.group2.case_study.repositories;

import com.group2.case_study.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdminUserRoleRepository extends JpaRepository<UserRole, Integer> {
}