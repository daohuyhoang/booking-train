package com.group2.case_study.repositories;

import com.group2.case_study.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    
    User findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.username = :userName")
    Integer findIdByUserName(String userName);
}