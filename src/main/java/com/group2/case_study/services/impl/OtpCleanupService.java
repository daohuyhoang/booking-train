package com.group2.case_study.services.impl;

import com.group2.case_study.repositories.IOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OtpCleanupService {

    @Autowired
    private IOtpRepository otpRepository;

    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void cleanupExpiredOtps() {
        try {
            LocalDateTime now = LocalDateTime.now();
            otpRepository.deleteExpiredOtps(now);
        } catch (Exception e) {
            System.err.println("Lỗi khi dọn dẹp OTP hết hạn: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
