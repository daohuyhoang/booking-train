package com.group2.case_study.controllers;

import com.group2.case_study.services.impl.OtpService;
import com.group2.case_study.services.impl.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (otpService.hasValidOtp(email)) {
                response.put("success", false);
                response.put("message", "Email này đã có mã OTP hợp lệ. Vui lòng kiểm tra email hoặc đợi mã hết hạn.");
                return ResponseEntity.ok(response);
            }

            String otpCode = otpService.generateOtpForRegistration(email);
            
            mailService.sendOtpEmail(email, otpCode);
            
            response.put("success", true);
            response.put("message", "Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư.");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Không thể gửi mã OTP. Vui lòng thử lại sau.");
            e.printStackTrace();
        }
        
        return ResponseEntity.ok(response);
    }

 
    @PostMapping("/resend")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String otpCode = otpService.generateOtpForRegistration(email);
            
            mailService.sendOtpEmail(email, otpCode);
            
            response.put("success", true);
            response.put("message", "Mã OTP mới đã được gửi đến email của bạn.");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Không thể gửi lại mã OTP. Vui lòng thử lại sau.");
            e.printStackTrace();
        }
        
        return ResponseEntity.ok(response);
    }


    @PostMapping("/check")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkOtp(@RequestParam String email, @RequestParam String code) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isValid = otpService.checkOtpValid(email, code);
            
            if (isValid) {
                response.put("success", true);
                response.put("message", "Mã OTP hợp lệ.");
            } else {
                response.put("success", false);
                response.put("message", "Mã OTP không hợp lệ hoặc đã hết hạn. Vui lòng kiểm tra lại.");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi kiểm tra OTP. Vui lòng thử lại.");
            e.printStackTrace();
        }
        
        return ResponseEntity.ok(response);
    }

   
    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestParam String email, @RequestParam String code) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isValid = otpService.verifyOtp(email, code);
            
            if (isValid) {
                response.put("success", true);
                response.put("message", "Mã OTP hợp lệ. Bạn có thể tiếp tục đăng ký.");
            } else {
                response.put("success", false);
                response.put("message", "Mã OTP không hợp lệ hoặc đã hết hạn. Vui lòng kiểm tra lại.");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi xác thực OTP. Vui lòng thử lại.");
            e.printStackTrace();
        }
        
        return ResponseEntity.ok(response);
    }
}
