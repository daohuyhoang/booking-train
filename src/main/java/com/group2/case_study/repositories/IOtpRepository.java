package com.group2.case_study.repositories;

import com.group2.case_study.models.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IOtpRepository extends JpaRepository<Otp, Long> {
    
    // Tìm OTP chưa sử dụng và còn hiệu lực theo email và type
    @Query("SELECT o FROM Otp o WHERE o.email = :email AND o.type = :type AND o.used = false AND o.expiresAt > :now ORDER BY o.createdAt DESC")
    Optional<Otp> findValidOtpByEmailAndType(@Param("email") String email, @Param("type") String type, @Param("now") LocalDateTime now);
    
    // Tìm OTP theo email, code và type
    @Query("SELECT o FROM Otp o WHERE o.email = :email AND o.code = :code AND o.type = :type AND o.used = false AND o.expiresAt > :now")
    Optional<Otp> findValidOtpByEmailAndCodeAndType(@Param("email") String email, @Param("code") String code, @Param("type") String type, @Param("now") LocalDateTime now);
    
    @Modifying
    @Transactional
    @Query("UPDATE Otp o SET o.used = true WHERE o.id = :id")
    void markAsUsed(@Param("id") Long id);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Otp o WHERE o.expiresAt < :now")
    void deleteExpiredOtps(@Param("now") LocalDateTime now);
    
    // Tìm tất cả OTP của một email theo type
    List<Otp> findByEmailAndTypeOrderByCreatedAtDesc(String email, String type);
}
