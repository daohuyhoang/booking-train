package com.group2.case_study.repositories;

import com.group2.case_study.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IAdminRoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}